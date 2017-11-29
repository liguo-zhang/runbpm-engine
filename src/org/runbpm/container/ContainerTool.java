package org.runbpm.container;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.runbpm.bpmn.definition.ActivityDefinition;
import org.runbpm.bpmn.definition.CallActivity;
import org.runbpm.bpmn.definition.EndEvent;
import org.runbpm.bpmn.definition.ExclusiveGateway;
import org.runbpm.bpmn.definition.InclusiveGateway;
import org.runbpm.bpmn.definition.ManualTask;
import org.runbpm.bpmn.definition.ParallelGateway;
import org.runbpm.bpmn.definition.ProcessDefinition;
import org.runbpm.bpmn.definition.ServiceTask;
import org.runbpm.bpmn.definition.StartEvent;
import org.runbpm.bpmn.definition.SubProcessDefinition;
import org.runbpm.bpmn.definition.UserTask;
import org.runbpm.bpmn.definition.UserTaskResource;
import org.runbpm.bpmn.definition.UserTaskResourceAssignment;
import org.runbpm.container.resource.ResourceEvalManager;
import org.runbpm.context.Configuration;
import org.runbpm.context.ProcessContextBean;
import org.runbpm.entity.ActivityInstance;
import org.runbpm.entity.ProcessInstance;
import org.runbpm.entity.VariableInstance;
import org.runbpm.exception.RunBPMException;
import org.runbpm.exception.RunBPMException.EXCEPTION_MESSAGE;
import org.runbpm.handler.resource.User;
import org.runbpm.persistence.EntityManager;

public class ContainerTool {

	// ���ݵ�ǰ�Ļ״̬���ж�ָ��������䶨����������ִ����
	// p7 2���֣��ֽ� ��Ʊ �� 13����+3����6���� 16���� 3��*16=48 �ֽ�
	// 1100 130�� 4�� 550 500 30��
	// �������� ��� offer���� 
	
		public static List<User> evalUserList(long processInstanceId,UserTask userTask){
			
			UserTaskResource userTaskResource = userTask.getUserTaskResource();
			UserTaskResourceAssignment resourceAssignment = userTaskResource.getUserTaskResourceAssignment();
			
			Map<String,VariableInstance> userTaskContext = Configuration.getContext().getEntityManager().loadVariableMap(processInstanceId);
			
			ProcessInstance processInstance = Configuration.getContext().getEntityManager().loadProcessInstance(processInstanceId);
			ProcessDefinition processDefinition= Configuration.getContext().getEntityManager().loadProcessModelByModelId(processInstance.getProcessModelId()).getProcessDefinition();
			
			if(resourceAssignment!=null){
					ProcessContextBean processContextBean = new ProcessContextBean();
					processContextBean.setProcessInstance(processInstance);
					processContextBean.setProcessDefinition(processDefinition);
					//processContextBean.setActivityInstance(activityInstance);
					processContextBean.setActivityDefinition(userTask);
					processContextBean.setVariableMap(userTaskContext);
					
					ResourceEvalManager rem = new ResourceEvalManager();
					List<User> userList =rem.getUserList(resourceAssignment, processContextBean);
					return userList;
			}
			return new ArrayList<User>();
		}

		public static ActivityContainer getActivityContainer(ActivityInstance activityInstance){
			ProcessDefinition processDefinition= Configuration.getContext().getEntityManager().loadProcessModelByModelId(activityInstance.getProcessModelId()).getProcessDefinition();
			ActivityDefinition activityDefinition = null;
			if(activityInstance.getParentActivityInstanceId()!=0){
				//��
				ActivityInstance parentActivityInstance = Configuration.getContext().getEntityManager().loadActivityInstance(activityInstance.getParentActivityInstanceId());
				SubProcessDefinition subProcessDefinition =processDefinition.getSubProcessActivityDefinition(parentActivityInstance.getSequenceBlockId());
				if(subProcessDefinition==null){
					throw new RunBPMException("cannot find subprocess .blockId["+activityInstance.getSubProcessBlockId()+"],current activity definitionid:["+activityInstance.getActivityDefinitionId()+"]");
				}
				activityDefinition = subProcessDefinition.getActivity(activityInstance.getActivityDefinitionId());
			}else{
				activityDefinition = processDefinition.getActivity(activityInstance.getActivityDefinitionId());
			}
			
			if(activityDefinition instanceof StartEvent 
					|| activityDefinition instanceof EndEvent 
					|| activityDefinition instanceof ManualTask 
					|| activityDefinition instanceof ParallelGateway
					|| activityDefinition instanceof ExclusiveGateway
					||activityDefinition instanceof InclusiveGateway
					){
				return new ActivityOfRouteContainer(activityDefinition,activityInstance); 
			}else if(activityDefinition instanceof UserTask){
				return new ActivityOfUserTaskContainer(activityDefinition,activityInstance);
			}else if(activityDefinition instanceof CallActivity){
				return new ActivityOfCallActivityContainer(activityDefinition,activityInstance);
			}else if(activityDefinition instanceof ServiceTask){
				return new ActivityOfServiceTaskContainer(activityDefinition,activityInstance);
			}else if(activityDefinition instanceof SubProcessDefinition){
				return new ActivityOfSubProcessContainer(activityDefinition,activityInstance);
			}
			
			throw new RunBPMException(RunBPMException.EXCEPTION_MESSAGE.Code_100001_Invalid_ActityDefinition_Type,"���Ϸ��Ļ���壬��������Ϊ:["+activityDefinition.getClass()+"]");
		}

		//�������
		public static FlowContainer getFlowContainer(ProcessInstance processInstance,ActivityInstance activityInstance){
			EntityManager entityManager = Configuration.getContext().getEntityManager();
			
			if(activityInstance ==null||activityInstance.getParentActivityInstanceId()==0){
				//��ͨ����
				ProcessContainer processContainer=  new ProcessContainer();
				processContainer.processInstance = processInstance;
				processContainer.processDefinition =entityManager.loadProcessModelByModelId(processInstance.getProcessModelId()).getProcessDefinition();
		
				return processContainer;
			}else{
				//XPDL����BPMNSubProcess
				ProcessDefinition processDefinition = entityManager.loadProcessModelByModelId(processInstance.getProcessModelId()).getProcessDefinition();
				
				ActivityInstance parentActivityInstance = entityManager.loadActivityInstance(activityInstance.getParentActivityInstanceId());
				SubProcessDefinition subProcessDefinition = processDefinition.getSubProcessActivityDefinition(parentActivityInstance.getSequenceBlockId());
				
				ActivityInstance activityInstanceOfSubProcess = entityManager.loadActivityInstance(activityInstance.getParentActivityInstanceId());
				
				SubProcessContainer subProcessContainer = new SubProcessContainer(activityInstanceOfSubProcess,subProcessDefinition);
				return subProcessContainer;
			}
		}
}
