package org.runbpm.service;

import java.io.File;
import java.util.EnumSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.runbpm.bpmn.definition.ActivityDefinition;
import org.runbpm.bpmn.definition.ProcessDefinition;
import org.runbpm.entity.ActivityHistory;
import org.runbpm.entity.ActivityInstance;
import org.runbpm.entity.EntityConstants;
import org.runbpm.entity.ProcessHistory;
import org.runbpm.entity.EntityConstants.ACTIVITY_STATE;
import org.runbpm.entity.EntityConstants.TASK_STATE;
import org.runbpm.handler.resource.User;
import org.runbpm.entity.ProcessInstance;
import org.runbpm.entity.ProcessModel;
import org.runbpm.entity.TaskHistory;
import org.runbpm.entity.TaskInstance;
import org.runbpm.entity.VariableInstance;
import org.runbpm.persistence.EntityManager;


public interface RunBPMService {
	
	/**
	 * �����������ڲ�����
	 * @param entityManager
	 */
	void setEntityManager(EntityManager entityManager);
	
	
	/**
	 * ����������������ģ���б����һ�����̶��屻�����Σ���ֻ�������°汾������ģ�塣<br>
	 * �����Ҫ��ȡ���а汾������ģ��,��ʹ��{@linke #loadProcessModelsWithAllVersions()}
	 * * <ul>
	 * 		<li>
	 * 		����ǵ�һ�ε��ô˷��������統��ʼ����������ʱ����������������ʹ�õ��ǳ־û��洢���������ݿ����NoSQL,ʹ�ø÷������ȡ����ڳ־û��洢�е�XML�ֶΣ�ͨ��JAXB���л�Ϊ���̶������<br>
	 * 		��ʼ�����������Web��ʽ����������գ�{@link org.runbpm.utils.InitRunBPMSpringContextServlet()}
	 * 		</li>
	 * 		<li>
	 * 		��ʼ����Ϻ�ÿһ�����̵��붼��ͬ�����������ģ���ڴ档ÿ�δ˷���ֻ���ȡ���������ڴ��ģ�塣
	 * 		</li>
	 * </ul>
	 * @return ����ģ���б�
	 */
	List<ProcessModel> loadProcessModels();
	
	
	/**
	 * ͨ��ָ�������̶���ID����ȡ�����̶���ID�����������ģ���б�
	 * @param processDefinitionId ���̶���ID
	 * @return ����ģ���б�
	 */
	List<ProcessModel> loadProcessModelsByProcessDefinitionId(String processDefinitionId);
	
	/**
	 * �����ļ����������̶��塣<br>
	 * ���磺<br>
	 * <pre>
		ClassPathResource classPathResource = new ClassPathResource(fileName,this.getClass());
		entityManager.initProcessDefinitionFromFile(classPathResource.getFile());
	</pre>
	 * @param file ����ϵͳ�ļ�����
	 */
	ProcessModel deployProcessDefinitionFromFile(File file);
	
	/**
	 * �����ַ����������̶��壬��Ȼ���ַ�������Ϊbpmn��ʽ�������webҳ���ϴ����̶����ļ�����ȡ����XML�ַ�����
	 * @param string ���̶����ַ���
	 * @return
	 */
	ProcessModel deployProcessDefinitionFromString(String string);
	
	/**
	 * �������̶�����������̶���
	 * @param processDefinition ���̶������
	 * @return
	 */
	ProcessModel deployProcessDefinition(ProcessDefinition processDefinition);
	
	/**
	 * �÷���ʹ�ø���ģ��ID����ָ��������ģ����󡣾�������̶��嵼��������ģ��Ļ��Ʋ��� {@link #loadLatestProcessMode(String)}
	 * @param processModelId ����ģ��ID,�ڹ�ϵ�����ݿ��У�Ϊ����ģ���������
	 * @return ����ģ�����ͨ��������ģ������getProcessDefinition()�������Ի�ȡ���̶��� {@link org.runbpm.bpmn.definition.ProcessDefinition}<br>
	 */
	ProcessModel loadProcessModelByModelId(long processModelId);
	
	
	/**
	 * �����̶����ļ��У�BPMN���̶���ID��XPathλ��Ϊ definitions/process@id��Ҳ����processԪ�ص�ID���ԡ�<br>
	 * ���̶�������뵽���ݿ�󣬻�������ģ���������һ����¼����ʹ�������̶���ID��ͬ�����̶��壬ÿ�ε��붼������һ����¼��<br>
	 * �÷���ʹ�ø���BPMN���̶���ID�������°汾������ģ�����<br>
	 * @param processModelId ����ģ��ID,�ڹ�ϵ�����ݿ��У�Ϊ����ģ���������<br>
	 * @return ����ģ�����ͨ��������ģ������getProcessDefinition()�������Ի�ȡ���̶��� {@link org.runbpm.bpmn.definition.ProcessDefinition}<br>
	 * 
	 */
	ProcessModel loadLatestProcessModel(String processModelId);
	
	/**
	 * ����ָ�������̶���ID����ȡ���뵽�����е����°汾�����̶�������������и����̶�������Ϊ����ģ�壬����ʵ����������ģ�������д��������С�
	 * @param processDefinitionId ָ��������ʵ��ID������Ϊ��
	 * @return ����ģ�����
	 */
	ProcessModel loadLatestProcessMode(String processDefinitionId);
	
	/**
	 * ����ָ�������̶���ID�����������潫��ȡ�ö�������°汾��������ģ�壩��Ȼ�󴴽�һ���µ�����ʵ����<br>
	 * ����ʵ���������״̬Ϊδ����״̬,״̬����Ϊ��{@link org.runbpm.entity.EntityConstants.PROCESS_STATE#NOT_STARTED}
	 * @param processDefinitionId
	 *          ָ��������ʵ��ID������Ϊ��
	 * @param creator ������������ݴ˼�¼����ʵ��������,����Ϊ��
	 * @return �´���������ʵ��
	 */
	ProcessInstance createProcessInstance(String processDefinitionId,String creator);
	
	/**
	 * �� {@link #createProcessInstance(String, String)}���ƣ�������Ϊָ��������ģ��ID��ͨ���˷������Ը����ϰ汾�����̶��崴������ʵ����
	 * @param processModelId ָ��������ģ��ID������Ϊ��
	 * @param creator ������������ݴ˼�¼����ʵ��������,����Ϊ��
	 * @return  �´���������ʵ��
	 */
	ProcessInstance createProcessInstance(long processModelId,String creator);
	
	/**
	 * ����ָ��������ʵ��ID����������ʵ����<br>
	 * ����ʵ���������״̬Ϊ����״̬,״̬����Ϊ��{@link org.runbpm.entity.EntityConstants.PROCESS_STATE#RUNNING}��<br>
	 * ���ǣ��������ʵ���м䲻����UserTask(�˹�����),���̽��Զ�����������״̬��<br>
	 * 
	 * ����������ǰ���������������̱���,���սӿ�: {@link #setProcessVariable(long, String, Object)} �� {@link #setProcessVariableMap(long, Map)}
	 * @param processInstanceId ����ʵ��ID��
	 * @return ����ʵ������
	 */
	ProcessInstance startProcessInstance(long processInstanceId);
	
	/**
	 * ����ָ��������ʵ��ID����ָ���Ļ����ID�������ǿ�ʼ�ڵ㣩��������ʵ����<br>
	 * ����ʵ���������״̬Ϊ����״̬,״̬����Ϊ��{@link org.runbpm.entity.EntityConstants.PROCESS_STATE#RUNNING}��<br>
	 * ���ǣ��������ʵ���м䲻����UserTask(�˹�����),���̽��Զ�����������״̬��<br>
	 * 
	 * ����������ǰ���������������̱���,���սӿ�: {@link #setProcessVariable(long, String, Object)} �� {@link #setProcessVariableMap(long, Map)}
	 * @param processInstanceId ����ʵ��ID��
	 * @param activityDefinitionId ָ���Ļ����ID��
	 * @return ����ʵ������
	 */
	ProcessInstance startProcessInstanceByActivityDefinitionId(long processInstanceId,String activityDefinitionId);
	
	/**
	 * �÷����ǽ���� {@link #createProcessInstance(String)} �� {@link #startProcessInstance(long)}��<br>
	 * �ǱȽϳ����ĳ����������к�����������<br>
	 * ����ʵ���������״̬Ϊ����״̬,״̬����Ϊ��{@link org.runbpm.entity.EntityConstants.PROCESS_STATE#RUNNING}��<br>
	 * ���ǣ��������ʵ���м䲻����UserTask(�˹�����),���̽��Զ�����������״̬��<br>
	 * @param processDefinitionId ��Ҫ�������������̶���ID
	 * @param creator ������������ݴ˼�¼����ʵ��������,����Ϊ��
	 * @return ����ʵ������
	 */
	ProcessInstance createAndStartProcessInstance(String processDefinitionId,String creator);
	
	/**
	 * �� {@link #createAndStartProcessInstance(String, String)}���ƣ�������Ϊָ��������ģ��ID��ͨ���˷������Ը����ϰ汾�����̶��崴������ʵ����
	 * @param processModelId ָ��������ģ��ID������Ϊ��
	 * @param creator ������������ݴ˼�¼����ʵ��������,����Ϊ��
	 * @return  �´���������ʵ��
	 */
	ProcessInstance createAndStartProcessInstance(long processModelId,String creator);
	
	/**
	 * �÷���ͬ {@link #createAndStartProcessInstance(String)},��������ʵ��ǰ�������̱���
	 * @param processDefinitionId
	 *          ��Ҫ�������������̶���ID
	 * @param creator ������������ݴ˼�¼����ʵ��������,����Ϊ��
	 * @param variableMap
	 *          ���̱���,��Map��ֵ���������Ϊ�����ı��������գ�{@link org.runbpm.entity.EntityConstants.VARIABLE_TYPE}
	 * @return
	 */
	ProcessInstance createAndStartProcessInstance(String processDefinitionId,String creator,Map<String,Object> variableMap);
	
	
	/**
	 * �÷���ͬ {@link #createAndStartProcessInstance(String, String, Map)},������Ϊָ��������ģ��ID��ͨ���˷������Ը����ϰ汾�����̶��崴������ʵ����
	 * @param processModelId
	 * @param creator
	 * @param variableMap
	 * @return
	 */
	ProcessInstance createAndStartProcessInstance(long processModelId,String creator,Map<String,Object> variableMap);
	
	List<ProcessInstance> listProcessInstanceByQueryString(String queryString);
	
	/**
	 * �������ָ���Ļʵ������APIһ������ ManualTask {@link org.runbpm.bpmn.definition.ManualTask} ���͵Ļ��<br>
	 * ����ûΪһ��UserTask���ͣ������������<br>
	 * ������һ��UserTaskʵ��ִ�����API����ʱ�����������潫�Զ�ִ�и÷�����
	 * ����ûʵ������δ������UserTask��ִ�д˷������׳��쳣��
	 * @param activityInstanceId ָ���Ļʵ��ID
	 */
	void completeActivityInstance(long activityInstanceId);
	
	/**
	 * ǿ����ֹ���ָ���Ļʵ������API�������������͵Ļ<br>
	 * ����ûΪһ��UserTask�����������ֹδ��ɵ������
	 * @param activityInstanceId ָ���Ļʵ��ID
	 */
	void terminateActivityInstance(long activityInstanceId);
	
	/**
	 * ǿ����ָֹ���Ļʵ��������ָ����·������ָ���Ļ���塣<br>
	 * {@link #terminateActivityInstance(long)}�����ᰴ�����̶������У����˷�������ʵ���˻ػ���ת����<br>
	 * @param activityInstanceId ָ���Ļʵ��
	 * @param targetActivityDefinitionId ָ��Ҫ��������һ������塣
	 */
	void terminateActivityInstance(long activityInstanceId,String targetActivityDefinitionId);
	
	/**
	 * ǿ����ֹ����ʵ��������ʵ���Լ����µĻʵ����������ʵ���Լ����̱������ݽ���ת�Ƶ���ʷ���С�
	 * @param processInstanceId ָ��������ʵ��ID��
	 */
	void terminateProcessInstance(long processInstanceId);
	
	/**
	 * ����ָ��������ʵ����ֻ�д��ڻ�Ծ״̬������ʵ�����ܱ����𣬷��������׳��쳣<br>
	 * ���̹������״̬�任Ϊ{@link org.runbpm.entity.EntityConstants.PROCESS_STATE#SUSPENDED}
	 * @param processInstanceId ָ��������ʵ��ID��
	 */
	void suspendProcessInstance(long processInstanceId);

	/**
	 * �ָ�ָ��������ʵ����ֻ�д��ڹ���״̬������ʵ�����ܱ����𣬷��������׳��쳣<br>
	 * ���̹������״̬�任Ϊ���̹���֮ǰ��״̬��{@link org.runbpm.entity.EntityConstants.PROCESS_STATE#NOT_STARTED}��{@link org.runbpm.entity.EntityConstants.PROCESS_STATE#RUNNING}�е�һ�֡�
	 * @param processInstanceId ָ��������ʵ��ID��
	 */
	void resumeProcessInstance(long processInstanceId);
	

	/**
	 * ����ָ���Ļʵ����ֻ�д��ڻ�Ծ״̬�Ļʵ�����ܱ����𣬷��������׳��쳣<br>
	 * ��������״̬�任Ϊ{@link org.runbpm.entity.EntityConstants.ACTIVITY_STATE#SUSPENDED}
	 * @param activityInstanceId ָ���Ļʵ��ID��
	 */
	void suspendActivityInstance(long activityInstanceId);
	

	/**
	 * �ָ�ָ���Ļʵ����ֻ�д��ڹ���״̬�Ļʵ�����ܱ����𣬷��������׳��쳣<br>
	 * ��������״̬�任Ϊ���̹���֮ǰ��״̬��{@link org.runbpm.entity.EntityConstants.ACTIVITY_STATE#NOT_STARTED}��{@link org.runbpm.entity.EntityConstants.ACTIVITY_STATE#RUNNING}�е�һ�֡�
	 * @param activityInstanceId ָ���Ļʵ��ID��
	 */
	void resumeActivityInstance(long activityInstanceId);
	
	
	/**
	 * ���һ���û����Ҳ����UerTaskΪ������ģʽ�����߳�Ϊ��һִ��ģʽ������ͨ���˷�����ȡ�����ִ��Ȩ��������֮���޷���ȡ������<br>
	 * �÷����������ڻ�ǩģʽ��
	 * @param userTaskId ָ��������ʵ��ID��
	 */
	void claimUserTask(long userTaskId);
	
	/**
	 * ���ĳһ���������������������������Ӱ�죺<br>
	 * ���һ���û����Ҳ����UerTaskΪ������ģʽ�����߳�Ϊ��һִ��ģʽ����ִ�д˷����������Զ����� {@link #completeActivityInstance(long)}������<br>
	 * ���һ���û����Ҳ����UerTaskΪ��ǩģʽ�������һ����ִ�к������Զ����� {@link #completeActivityInstance(long)}���� ��<br>
	 * @param userTaskId ָ��������ʵ��ID��
	 */
	void completeUserTask(long userTaskId);
	
	/**
	 * ��ֹĳһ������ {@link #completeUserTask(long)} Ψһ�Ĳ�ͬ��������UserTask���¼���״̬λ��һ����
	 * ��ֹ���������������Ӱ����� {@link #completeUserTask(long)}
	 * @param userTaskId ָ��������ʵ��ID��
	 */
	void terminateUserTask(long userTaskId);
	
	/**
	 * ����ָ��������ʵ����ֻ�д��ڻ�Ծ״̬������ʵ�����ܱ����𣬷��������׳��쳣<br>
	 * ����������״̬�任Ϊ{@link org.runbpm.entity.EntityConstants.TASK_STATE#SUSPENDED}
	 * @param userTaskId ָ��������ʵ��ID��
	 */
	void suspendUserTask(long userTaskId);
	

	/**
	 * �ָ�ָ��������ʵ����ֻ�д��ڹ���״̬������ʵ�����ܱ����𣬷��������׳��쳣<br>
	 * ����������״̬�任Ϊ���̹���֮ǰ��״̬��{@link org.runbpm.entity.EntityConstants.TASK_STATE#NOT_STARTED}��{@link org.runbpm.entity.EntityConstants.TASK_STATE#RUNNING}�е�һ�֡�
	 * @param userTaskId ָ��������ʵ��ID��
	 */
	void resumeUserTask(long userTaskId);
	
	/**
	 * ȡ��һ�����񡣸÷���ֻ�Ὣ����״̬��Ϊȡ��������������������ִ�С������Ҫ��������ɾ������ʹ��{@ #removeUserTask(long)}����
	 * @param userTaskId ��Ҫȡ��������ID
	 */
	void cancelUserTask(long userTaskId);
	
	/**
	 * ɾ��һ�����������Ҫ����������ͬʱ��Ϊȡ������ʹ��{@ #cancelUserTask(long)}����
	 * @param userTaskId ��Ҫɾ��������ID
	 */
	void removeUserTask(long userTaskId);
	
	
	/**
	 * �����������·���һ��ִ���ˡ��÷����Ὣ����ֱ��ִ���ˡ�<br>
	 * �����Ҫ����ԭִ���˵����񣬿��ۺ� {@ link #cancelUserTask(long)} ��{@ link #addUserTask(long, long)}��������ʵ�֡� 
	 * @param userTaskId ָ��������ID
	 * @param userId ִ����ID
	 */
	void setAssignee(long userTaskId,String userId);
	
	/**
	 * ��̬����һ������
	 * @param activityInstanceID ��Ӧ�Ļʵ�����û����������˹����������׳��쳣��
	 * @param userId ִ����ID
	 * @param state ���������״̬������ǻ�ǩģʽ��Ӧ��ѡ��{@link EntityConstants.TASK_STATE#RUNNING};����Ǿ���(������ģʽ����Ӧ��ѡ��{@link EntityConstants.TASK_STATE#NOT_STARTED}
	 * @return �������Ļʵ��
	 * 
	 */
	TaskInstance addUserTask(long activityInstanceId,String userId,EntityConstants.TASK_STATE state);
	
	/**
	 * ���һ���û����Ҳ����UerTaskΪ������ģʽ�����߳�Ϊ��һִ��ģʽ������ͨ�� {@link #claimUserTask(long)}��ȡ�����ִ��Ȩ�󣬿���ͨ���˷������������·Żأ����е��˿�������������<br>
	 * ���һ���û����Ҳ����UerTaskΪ��ǩģʽ��������ִ�и÷�����<br>
	 * @param userTaskId ָ��������ʵ��ID��
	 */
	void putbackUserTask(long userTaskId);
	
	/**
	 * ���һ���û����Ҳ����UerTaskΪ������ģʽ�����߳�Ϊ��һִ��ģʽ������ͨ�� {@link #claimUserTask(long)}��ȡ�����ִ��Ȩ�󣬿���ͨ���˷������������·������һ���ˡ�<br>
	 * ���һ���û����Ҳ����UerTaskΪ��ǩģʽ��Ҳ����ͨ���˷������������·������һ���ˡ�<br>
	 * @param userTaskId ָ��������ʵ��ID��
	 * @param userId �û�ID��
	 */
	void setUserTaskAssignee(long userTaskId,String userId) ;
	
	/**
	 * ��ȡָ��������ʵ��
	 * @param processInstanceId ָ��������ʵ��ID��
	 * @return ����ʵ������
	 */
	ProcessInstance loadProcessInstance(long processInstanceId);
	
	/**
	 * ��ȡָ���Ļʵ��
	 * @param activityInstanceId ָ���Ļʵ��ID��
	 * @return ����ʵ������
	 */
	ActivityInstance loadActivityInstance(long activityInstanceId);
	
	/**
	 * ��ȡָ���Ĺ�����ʵ��
	 * @param taskInstanceId ָ���Ĺ�����ʵ��ID��
	 * @return ������ʵ������
	 */
	TaskInstance loadTaskInstance(long taskInstanceId);

	/**
	 * ��ȡָ������ʵ���µ����лʵ���б�
	 * @param processInstId ָ��������ʵ��ID��
	 * @return �ʵ������
	 */
	List<ActivityInstance>  listActivityInstanceByProcessInstId(long processInstId);

	
	/**
	 * ��ȡָ������ʵ��������ָ���״̬�Ļʵ���б�
	 * @param processInstanceId ָ��������ʵ��ID��
	 * @param stateSet �״̬�����б�
	 * ������
	 * <pre>
		EnumSet<ACTIVITY_STATE> set = EnumSet.noneOf(ACTIVITY_STATE.class);  
		set.add(ACTIVITY_STATE.NOT_STARTED);  
		set.add(ACTIVITY_STATE.RUNNING);
		set.add(ACTIVITY_STATE.SUSPENDED);
		</pre>
	 * @return �ʵ���б�
	 */
	List<ActivityInstance> listActivityInstanceByProcessInstIdAndState(long processInstanceId,EnumSet<ACTIVITY_STATE> stateSet);
	/**
	 * ��ȡָ������ʵ���£�ָ����SubProcess�µģ�ָ���Ļ����Ļʵ���б�<br>
	 * @param processInstanceId ָ��������ʵ��ID��
	 * @param subProcessId SubProcessId ����XPDL�淶��ΪActivitySet����BPMN�淶��ΪSubProcess��
	 * @param stateSet �״̬�����б�
	 * ������
	 * <pre>
		EnumSet<ACTIVITY_STATE> set = EnumSet.noneOf(ACTIVITY_STATE.class);  
		set.add(ACTIVITY_STATE.NOT_STARTED);  
		set.add(ACTIVITY_STATE.RUNNING);
		set.add(ACTIVITY_STATE.SUSPENDED);
		</pre>
	 * @return �ʵ���б�
	 */
	List<ActivityInstance> listActivityInstanceByProcessInstIdSubrocessIdAndState(long processInstanceId,String subProcessId,EnumSet<ACTIVITY_STATE> stateSet);
	
	/**
	 * ��ȡָ������ʵ���£�ָ���Ļ����Ļʵ���б�
	 * @param processInstanceId ָ��������ʵ��ID��
	 * @param activityDefinitionId ָ���Ļ����ID��
	 * @return �ʵ���б�
	 */
	List<ActivityInstance>  listActivityInstanceByActivityDefId(long processInstanceId,String activityDefinitionId);
	/**
	 * ��ȡָ������ʵ���£�ָ���Ļ���壬ָ���Ļ״̬�Ļʵ���б�
	 * 
	 * @param processInstanceId ָ��������ʵ��ID��
	 * @param activityDefinitionId ָ���Ļ����ID��
	 * @param stateSet �״̬�����б�
	 * ������
	 * <pre>
		EnumSet<ACTIVITY_STATE> set = EnumSet.noneOf(ACTIVITY_STATE.class);  
		set.add(ACTIVITY_STATE.NOT_STARTED);  
		set.add(ACTIVITY_STATE.RUNNING);
		set.add(ACTIVITY_STATE.SUSPENDED);
		</pre>
	 * @return �ʵ���б�
	 */
	List<ActivityInstance> listActivityInstanceByActivityDefIdAndState(long processInstanceId,String activityDefinitionId,EnumSet<ACTIVITY_STATE> stateSet);
	
	/**
	 * ��ȡָ���ʵ���µ������������б�
	 * @param activityInstanceId ָ���Ļʵ��ID
	 * @return ��������б�
	 */
	List<TaskInstance> listTaskInstanceByActivityInstId(long activityInstanceId);
	
	/**
	 * ��ȡָ������ʵ���µĵ������������б�
	 * @param processInstanceId ָ������
	 * @return �������б�
	 */
	List<TaskInstance> listTaskInstanceByProcessInstId(long processInstanceId);
	
	/**
	 * ��ȡָ���û��ģ�ָ���ʵ���ģ��ҷ���ָ��״̬���������б�<br>
	 * @param activityInstanceId ָ���Ļʵ��ID
	 * @param stateSet �ò����������������ɲ��գ� {@link #listTaskInstanceByUserIdAndState(String, EnumSet)}
	 * @return �������б�
	 */
	List<TaskInstance> listTaskInstanceByActivityInstIdAndState(long activityInstanceId,EnumSet<TASK_STATE> stateSet);
	
	/**
	 * ��ȡָ���û��ģ�ָ��״̬���������б�ʹ�ø�API��ȡ�Ĺ��������ڵ����̶�������֮�У���û��ת�Ƶ���ʷ���С�
	 * 
	 * @param userId ָ�����û�״̬
	 * @param stateSet ָ����״̬ <br>
	 * ��1����ȡ���л�Ծ״̬��������������
	  		<pre>
			EnumSet<EntityConstants.TASK_STATE> stateSet = EnumSet.noneOf(EntityConstants.TASK_STATE.class);  
			stateSet.add(EntityConstants.TASK_STATE.NOT_STARTED);  
			stateSet.add(EntityConstants.TASK_STATE.RUNNING);
            </pre>
	 * ��2����ȡ���н���״̬��������������<br>
	 		<pre>
	 		EnumSet<EntityConstants.TASK_STATE> stateSet = EnumSet.noneOf(EntityConstants.TASK_STATE.class); 
	 		stateSet.add(EntityConstants.TASK_STATE.TERMINATED);
	 		stateSet.add(EntityConstants.TASK_STATE.COMPLETED);
            </pre>
	 * ��3����ȡ���ڹ���״̬��������������<br>
			<pre>
			EnumSet<EntityConstants.TASK_STATE> stateSet = EnumSet.noneOf(EntityConstants.TASK_STATE.class);  
			stateSet.add(EntityConstants.TASK_STATE.SUSPENDED);
			</pre>
	 * @return �������б�
	 */
	List<TaskInstance> listTaskInstanceByUserIdAndState(String userId,EnumSet<TASK_STATE> stateSet);
	
	/**
	 * ��ȡָ���û������е������
	 * @param userId �û�ID
	 * @return �������б�
	 */
	List<TaskInstance> listTaskInstanceByUserId(String userId) ;
	


	/**
	 * Ϊָ��������ʵ��������һ�����̱���
	 * @param processInstanceId ָ��������ʵ��ID
	 * @param name ����
	 * @param value ֵ
	 */
	void setProcessVariable(long processInstanceId, String name, Object value);
	
	/**
	 * Ϊָ��������ʵ�������ö�����̱�����
	 * @param processInstanceId ָ��������ʵ��ID
	 * @param dataFieldMap ������̱���
	 */
	void setProcessVariableMap(long processInstanceId, Map<String, Object>dataFieldMap);
	
	/**
	 * ��ȡָ�����̵ĵ����̱����б�
	 * @param processInstanceId
	 * @return ���յı���ֵ��ʹ�� {@link org.runbpm.entity.VariableInstance#getValue()}���
	 */
	Map<String,VariableInstance> loadVariableMap(long processInstanceId);
	
	/**
	 * ��ѯ����ָ�������ߣ�������������ʵ���б�<br>
	 * ����ʵ���Ĵ������ֶΣ����ݴ�������APIʱ�����븳ֵ��
	 * @param creator ������
	 * @return ����ʵ���б�
	 */
	List<ProcessInstance> listProcessInstanceByCreator(String creator);
	
	/**
	 * ��ѯ����ָ�������ߣ���������������ʷ�б�<br>
	 * ����ʵ���Ĵ������ֶΣ����ݴ�������APIʱ�����븳ֵ��
	 * @param creator ������
	 * @return ������ʷ�б�
	 */
	List<ProcessHistory> listProcessHistoryByCreator(String creator);
	
	/**
	 * ��ȡָ����������ʷ
	 * @param processHistoryId
	 * @return ������ʷ����
	 */
	ProcessHistory loadProcessHistory(long processHistoryId);
	
	/**
	 * ��ȡָ���Ļ��ʷ
	 * @param activityHistoryId ���ʷID
	 * @return ���ʷ����
	 */
	ActivityHistory loadActivityHistory(long activityHistoryId);
	
	/**
	 * ��ȡ���������ʷ
	 * @param taskHistoryId ������ʷ����ID 
	 * @return ������ʷ����
	 */
	TaskHistory loadTaskHistory(long taskHistoryId);
	
	/**
	 * ��ȡָ��������ʷ�µ����л�����б�
	 * @param processHistoryId ָ����������ʷID
	 * @return ���ʷ�����б�
	 */
	List<ActivityHistory> listActivityHistoryByProcessInstId(long processHistoryId);
	
	/**
	 * ����ָ��������ʷ��¼�µģ�ָ�������Ļʵ����ʷ 
	 * @param processHistoryId ������ʷ��¼
	 * @param activityDefinitionId �����
	 * @return ָ���Ļʵ����ʷ
	 */
	List<ActivityHistory> listActivityHistoryByActivityDefId(long processHistoryId, String activityDefinitionId);
	
	/**
	 * ����ָ��������ʷ�µģ�����������ʷ�б�
	 * @param processHistoryId ������ʷID
	 * @return ������ʷ�б�
	 */
	List<TaskHistory> listTaskHistoryByProcessInstId(long processHistoryId);
	
	/**
	 * ����ָ�����ʷ�µģ�����������ʷ�б�
	 * @param activityHistoryId ���ʷID
	 * @return ������ʷ�б�
	 */
	List<TaskHistory> listTaskHistoryByActivityInstId(long activityHistoryId);
	
	
	/**
	 * ��ĳһ����������ʱ����ȡ��ǰ�ʵ����һ�����ܵ���Ļ���塣���������ڼ���ʱ���ῼ�����������ı�����
	 * 
	 * �����Ҫ�����̶�����棬��ȡָ���Ļ����δ�����ܵ�������л���壨���������������ı���������������ת�ȳ��������ο�{@link org.runbpm.bpmn.definition.ProcessDefinition#listReachableActivitySet(ActivityDefinition)}
	 * @param activityInstanceId �ʵ��ID
	 * @return ���Ե���Ļ����
	 */
	Set<ActivityDefinition> listReachableActivitySet(long activityInstanceId);
	
	
	/**
	 * ����ָ��������ʵ�����Լ�������ʵ���Ļ���壬ͨ���������жϣ���������䶨����������ִ����
	 * @param processInstanceId ָ��������ʵ��
	 * @param activityDefinitionId ������ʵ���Ļ����
	 * @return ͨ���������жϣ���������䶨����������ִ����
	 */
	List<User> evalUserList(long processInstanceId,String activityDefinitionId);

	/**
	 * ����������������ģ���б����һ�����̶��屻�����Σ���᷵�ض�����¼��
	 * * <ul>
	 * 		<li>
	 * 		����ǵ�һ�ε��ô˷��������統��ʼ����������ʱ����������������ʹ�õ��ǳ־û��洢���������ݿ����NoSQL,ʹ�ø÷������ȡ����ڳ־û��洢�е�XML�ֶΣ�ͨ��JAXB���л�Ϊ���̶������<br>
	 * 		��ʼ�����������Web��ʽ����������գ�{@link org.runbpm.utils.InitRunBPMSpringContextServlet()}
	 * 		</li>
	 * 		<li>
	 * 		��ʼ����Ϻ�ÿһ�����̵��붼��ͬ�����������ģ���ڴ档ÿ�δ˷���ֻ���ȡ���������ڴ��ģ�塣
	 * 		</li>
	 * </ul>
	 * @return ����ģ���б�
	 */
	List<ProcessModel> loadProcessModelsWithAllVersions();	
	
}
