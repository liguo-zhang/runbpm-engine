package org.runbpm.container.usertask;

import java.io.IOException;
import java.util.EnumSet;
import java.util.List;

import junit.framework.Assert;

import org.junit.Test;
import org.runbpm.RunBPMTestCase;
import org.runbpm.bpmn.definition.ProcessDefinition;
import org.runbpm.container.ProcessContainer;
import org.runbpm.container.UserTaskContainer;
import org.runbpm.context.Configuration;
import org.runbpm.entity.ActivityInstance;
import org.runbpm.entity.EntityConstants;
import org.runbpm.entity.ProcessInstance;
import org.runbpm.entity.ProcessModel;
import org.runbpm.entity.TaskInstance;
import org.runbpm.entity.EntityConstants.ACTIVITY_STATE;
import org.runbpm.entity.EntityConstants.PROCESS_STATE;
import org.runbpm.exception.RunBPMException;
import org.runbpm.persistence.EntityManager;
import org.runbpm.persistence.memory.MemoryEntityManagerImpl;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.core.io.ClassPathResource;

public class ExpressUserTaskContainerTest extends RunBPMTestCase{

	
	@Test
	public void test() throws Exception{
		
		MemoryEntityManagerImpl entityManager = (MemoryEntityManagerImpl) Configuration.getContext().getEntityManager();
		entityManager.clearMemory();
		
		String fileName = this.getBPMNXMLName();
		ClassPathResource classPathResource = new ClassPathResource(fileName,this.getClass());
		entityManager.deployProcessDefinitionFromFile(classPathResource.getFile());
		
		ProcessContainer processInstanceContainer = ProcessContainer.getProcessContainerForNewInstance();
		ProcessInstance processInstance = processInstanceContainer.createInstance(fileName);
		
		Long processInstanceId = processInstance.getId();
		
		//���ñ���
		entityManager.setProcessVariable(processInstanceId, "userid", "user5");
		entityManager.setProcessVariable(processInstanceId, "groupid", "groupC");
		
		// ��ʼ�ڵ�1������   �� theStart�����״̬�� u1������״̬�� 
		processInstanceContainer.start();
		
		ActivityInstance u1_instance = entityManager.listActivityInstanceByActivityDefId(processInstanceId, "u1").iterator().next();
		
		Assert.assertEquals("" , u1_instance.getState(),ACTIVITY_STATE.RUNNING);
		
		List<TaskInstance> taskInstance_u1 = entityManager.listTaskInstanceByActivityInstId(u1_instance.getId());
		Assert.assertEquals("" , taskInstance_u1.size(),1);
		Assert.assertEquals("" , taskInstance_u1.get(0).getState(),EntityConstants.TASK_STATE.NOT_STARTED);
		
		
		List<TaskInstance> taskInstance_user9 = entityManager.listTaskInstanceByUserIdAndState("user9", null);
		Assert.assertEquals("" , taskInstance_user9.size(),1);
		Assert.assertEquals("" , taskInstance_user9.get(0).getState(),EntityConstants.TASK_STATE.NOT_STARTED);
		
		UserTaskContainer userTaskContainer = new UserTaskContainer(taskInstance_user9.get(0));
		userTaskContainer.claim();
		
		//״̬�ı�
		List<TaskInstance> taskInstance_user9_after_claim = entityManager.listTaskInstanceByUserIdAndState("user9", null);
		Assert.assertEquals("" , taskInstance_user9_after_claim.size(),1);
		Assert.assertEquals("" , taskInstance_user9_after_claim.get(0).getState(),EntityConstants.TASK_STATE.RUNNING);
		
		UserTaskContainer userTaskContainer_after_claim = new UserTaskContainer(taskInstance_user9_after_claim.get(0));
		userTaskContainer_after_claim.complete();
		
		//��ʼ�ڵ�1�������� theStart�����״̬�� u1�����״̬��u2������״̬��
		ActivityInstance u2_instance = entityManager.listActivityInstanceByActivityDefId(processInstanceId, "u2").iterator().next();
		Assert.assertEquals("" , u2_instance.getState(),ACTIVITY_STATE.RUNNING);
		
		List<TaskInstance> taskInstance_u2 = entityManager.listTaskInstanceByActivityInstId(u2_instance.getId());
		//user5 ����1������
		Assert.assertEquals("" , taskInstance_u2.size(),1);
		Assert.assertEquals("" , taskInstance_u2.get(0).getState(),EntityConstants.TASK_STATE.NOT_STARTED);
		
		UserTaskContainer taskInstance_u2_0 = new UserTaskContainer(taskInstance_u2.get(0));
		taskInstance_u2_0.claim();
		taskInstance_u2_0.complete();
		
		//��ʼ�ڵ�2�������� theStart�����״̬�� u1�����״̬��u2�����״̬��u3(��ʼ״̬) theEnd(���״̬)
		ActivityInstance u3_instance = entityManager.listActivityInstanceByActivityDefId(processInstanceId, "u3").iterator().next();
		Assert.assertEquals("" , u3_instance.getState(),ACTIVITY_STATE.RUNNING);
		
		List<TaskInstance> taskInstance_u3 = entityManager.listTaskInstanceByActivityInstId(u3_instance.getId());
		//groupC����1������
		Assert.assertEquals("" , taskInstance_u3.size(),3);
		Assert.assertEquals("" , taskInstance_u3.get(0).getState(),EntityConstants.TASK_STATE.NOT_STARTED);
		Assert.assertEquals("" , taskInstance_u3.get(1).getState(),EntityConstants.TASK_STATE.NOT_STARTED);
		Assert.assertEquals("" , taskInstance_u3.get(2).getState(),EntityConstants.TASK_STATE.NOT_STARTED);
		
		UserTaskContainer taskInstance_u3_0 = new UserTaskContainer(taskInstance_u3.get(0));
		taskInstance_u3_0.claim();
		taskInstance_u3_0.complete();
		
		//��ʼ�ڵ�3�������� theStart�����״̬�� u1�����״̬��u2�����״̬��u3(���״̬) u4(��ʼ״̬) theEnd(���״̬)
		ActivityInstance u4_instance = entityManager.listActivityInstanceByActivityDefId(processInstanceId, "u4").iterator().next();
		Assert.assertEquals("" , u4_instance.getState(),ACTIVITY_STATE.RUNNING);
		
		List<TaskInstance> taskInstance_u4 = entityManager.listTaskInstanceByActivityInstId(u4_instance.getId());
		//user5 ����1������
		Assert.assertEquals("" , taskInstance_u4.size(),2);
		Assert.assertEquals("" , taskInstance_u4.get(0).getState(),EntityConstants.TASK_STATE.NOT_STARTED);
		Assert.assertEquals("" , taskInstance_u4.get(1).getState(),EntityConstants.TASK_STATE.NOT_STARTED);
		
		UserTaskContainer taskInstance_u4_0 = new UserTaskContainer(taskInstance_u4.get(0));
		taskInstance_u4_0.claim();
		taskInstance_u4_0.complete();
		
		ActivityInstance ac_theend = entityManager.listActivityInstanceByActivityDefId(processInstanceId, "theEnd").iterator().next();
		Assert.assertEquals("" , ac_theend.getState(),ACTIVITY_STATE.COMPLETED);
		
		//�������
		ProcessInstance newProcessInstance = entityManager.getProcessInstance(processInstanceId);
		Assert.assertEquals("" , newProcessInstance.getState(),PROCESS_STATE.COMPLETED);
		
	}
}
