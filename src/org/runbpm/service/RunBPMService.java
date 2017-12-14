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
	 * 工作流引擎内部方法
	 * @param entityManager
	 */
	void setEntityManager(EntityManager entityManager);
	
	
	/**
	 * 获得流程引擎的流程模板列表，如果一个流程定义被导入多次，则只返回最新版本的流程模板。<br>
	 * 如果需要获取所有版本的流程模板,则使用{@linke #loadProcessModelsWithAllVersions()}
	 * * <ul>
	 * 		<li>
	 * 		如果是第一次调用此方法（例如当初始化流程引擎时），而且流程引擎使用的是持久化存储，例如数据库或者NoSQL,使用该方法会读取存放在持久化存储中的XML字段，通过JAXB序列化为流程定义对象。<br>
	 * 		初始化流程引擎的Web方式启动插件参照：{@link org.runbpm.utils.InitRunBPMSpringContextServlet()}
	 * 		</li>
	 * 		<li>
	 * 		初始化完毕后，每一次流程导入都会同步流程引擎的模板内存。每次此方法只会读取流程引擎内存的模板。
	 * 		</li>
	 * </ul>
	 * @return 流程模板列表
	 */
	List<ProcessModel> loadProcessModels();
	
	
	/**
	 * 通过指定的流程定义ID，获取该流程定义ID所部署的流程模板列表
	 * @param processDefinitionId 流程定义ID
	 * @return 流程模板列表
	 */
	List<ProcessModel> loadProcessModelsByProcessDefinitionId(String processDefinitionId);
	
	/**
	 * 根据文件对象导入流程定义。<br>
	 * 例如：<br>
	 * <pre>
		ClassPathResource classPathResource = new ClassPathResource(fileName,this.getClass());
		entityManager.initProcessDefinitionFromFile(classPathResource.getFile());
	</pre>
	 * @param file 操作系统文件对象
	 */
	ProcessModel deployProcessDefinitionFromFile(File file);
	
	/**
	 * 根据字符串导入流程定义，当然该字符串必须为bpmn格式，例如从web页面上传流程定义文件，读取到的XML字符串。
	 * @param string 流程定义字符串
	 * @return
	 */
	ProcessModel deployProcessDefinitionFromString(String string);
	
	/**
	 * 根据流程定义对象导入流程定义
	 * @param processDefinition 流程定义对象
	 * @return
	 */
	ProcessModel deployProcessDefinition(ProcessDefinition processDefinition);
	
	/**
	 * 该方法使用根据模板ID加载指定的流程模板对象。具体的流程定义导入与流程模板的机制参照 {@link #loadLatestProcessMode(String)}
	 * @param processModelId 流程模板ID,在关系型数据库中，为流程模板的主键。
	 * @return 流程模板对象。通过该流程模板对象的getProcessDefinition()方法可以获取流程定义 {@link org.runbpm.bpmn.definition.ProcessDefinition}<br>
	 */
	ProcessModel loadProcessModelByModelId(long processModelId);
	
	
	/**
	 * 在流程定义文件中，BPMN流程定义ID的XPath位置为 definitions/process@id，也就是process元素的ID属性。<br>
	 * 流程定义对象导入到数据库后，会在流程模板表里生成一条记录。即使对于流程定义ID相同的流程定义，每次导入都会生成一条记录。<br>
	 * 该方法使用根据BPMN流程定义ID加载最新版本的流程模板对象。<br>
	 * @param processModelId 流程模板ID,在关系型数据库中，为流程模板的主键。<br>
	 * @return 流程模板对象。通过该流程模板对象的getProcessDefinition()方法可以获取流程定义 {@link org.runbpm.bpmn.definition.ProcessDefinition}<br>
	 * 
	 */
	ProcessModel loadLatestProcessModel(String processModelId);
	
	/**
	 * 根据指定的流程定义ID，获取导入到引擎中的最新版本的流程定义对象，在引擎中该流程定义对象称为流程模板，流程实例根据流程模板来进行创建和运行。
	 * @param processDefinitionId 指定的流程实例ID，不能为空
	 * @return 流程模板对象
	 */
	ProcessModel loadLatestProcessMode(String processDefinitionId);
	
	/**
	 * 根据指定的流程定义ID，工作流引擎将获取该定义的最新版本（即流程模板），然后创建一个新的流程实例。<br>
	 * 流程实例创建后的状态为未启动状态,状态常量为：{@link org.runbpm.entity.EntityConstants.PROCESS_STATE#NOT_STARTED}
	 * @param processDefinitionId
	 *          指定的流程实例ID，不能为空
	 * @param creator 工作流引擎根据此记录流程实例创建者,允许为空
	 * @return 新创建的流程实例
	 */
	ProcessInstance createProcessInstance(String processDefinitionId,String creator);
	
	/**
	 * 与 {@link #createProcessInstance(String, String)}类似，但参数为指定的流程模板ID，通过此方法可以根据老版本的流程定义创建流程实例。
	 * @param processModelId 指定的流程模板ID，不能为空
	 * @param creator 工作流引擎根据此记录流程实例创建者,允许为空
	 * @return  新创建的流程实例
	 */
	ProcessInstance createProcessInstance(long processModelId,String creator);
	
	/**
	 * 根据指定的流程实例ID，启动流程实例。<br>
	 * 流程实例启动后的状态为运行状态,状态常量为：{@link org.runbpm.entity.EntityConstants.PROCESS_STATE#RUNNING}。<br>
	 * 但是，如果流程实例中间不包含UserTask(人工任务),流程将自动运行至结束状态。<br>
	 * 
	 * 流程在启动前，可以先设置流程变量,参照接口: {@link #setProcessVariable(long, String, Object)} 和 {@link #setProcessVariableMap(long, Map)}
	 * @param processInstanceId 流程实例ID。
	 * @return 流程实例对象
	 */
	ProcessInstance startProcessInstance(long processInstanceId);
	
	/**
	 * 根据指定的流程实例ID，以指定的活动定义ID（不必是开始节点）启动流程实例。<br>
	 * 流程实例启动后的状态为运行状态,状态常量为：{@link org.runbpm.entity.EntityConstants.PROCESS_STATE#RUNNING}。<br>
	 * 但是，如果流程实例中间不包含UserTask(人工任务),流程将自动运行至结束状态。<br>
	 * 
	 * 流程在启动前，可以先设置流程变量,参照接口: {@link #setProcessVariable(long, String, Object)} 和 {@link #setProcessVariableMap(long, Map)}
	 * @param processInstanceId 流程实例ID。
	 * @param activityDefinitionId 指定的活动定义ID。
	 * @return 流程实例对象
	 */
	ProcessInstance startProcessInstanceByActivityDefinitionId(long processInstanceId,String activityDefinitionId);
	
	/**
	 * 该方法是结合了 {@link #createProcessInstance(String)} 和 {@link #startProcessInstance(long)}，<br>
	 * 是比较常见的场景，即运行后立即启动。<br>
	 * 流程实例启动后的状态为运行状态,状态常量为：{@link org.runbpm.entity.EntityConstants.PROCESS_STATE#RUNNING}。<br>
	 * 但是，如果流程实例中间不包含UserTask(人工任务),流程将自动运行至结束状态。<br>
	 * @param processDefinitionId 需要运行启动的流程定义ID
	 * @param creator 工作流引擎根据此记录流程实例创建者,允许为空
	 * @return 流程实例对象
	 */
	ProcessInstance createAndStartProcessInstance(String processDefinitionId,String creator);
	
	/**
	 * 与 {@link #createAndStartProcessInstance(String, String)}类似，但参数为指定的流程模板ID，通过此方法可以根据老版本的流程定义创建流程实例。
	 * @param processModelId 指定的流程模板ID，不能为空
	 * @param creator 工作流引擎根据此记录流程实例创建者,允许为空
	 * @return  新创建的流程实例
	 */
	ProcessInstance createAndStartProcessInstance(long processModelId,String creator);
	
	/**
	 * 该方法同 {@link #createAndStartProcessInstance(String)},但在流程实例前设置流程变量
	 * @param processDefinitionId
	 *          需要运行启动的流程定义ID
	 * @param creator 工作流引擎根据此记录流程实例创建者,允许为空
	 * @param variableMap
	 *          流程变量,该Map的值对象的类型为常见的变量，参照：{@link org.runbpm.entity.EntityConstants.VARIABLE_TYPE}
	 * @return
	 */
	ProcessInstance createAndStartProcessInstance(String processDefinitionId,String creator,Map<String,Object> variableMap);
	
	
	/**
	 * 该方法同 {@link #createAndStartProcessInstance(String, String, Map)},但参数为指定的流程模板ID，通过此方法可以根据老版本的流程定义创建流程实例。
	 * @param processModelId
	 * @param creator
	 * @param variableMap
	 * @return
	 */
	ProcessInstance createAndStartProcessInstance(long processModelId,String creator,Map<String,Object> variableMap);
	
	List<ProcessInstance> listProcessInstanceByQueryString(String queryString);
	
	/**
	 * 正常完成指定的活动实例。该API一般用于 ManualTask {@link org.runbpm.bpmn.definition.ManualTask} 类型的活动。<br>
	 * 如果该活动为一个UserTask类型，将会产生任务。<br>
	 * 当最有一个UserTask实例执行完成API调用时，工作流引擎将自动执行该方法。
	 * 如果该活动实例下有未结束的UserTask，执行此方法将抛出异常。
	 * @param activityInstanceId 指定的活动实例ID
	 */
	void completeActivityInstance(long activityInstanceId);
	
	/**
	 * 强制终止完成指定的活动实例。该API适用于所有类型的活动<br>
	 * 如果该活动为一个UserTask，则会首先终止未完成的任务项。
	 * @param activityInstanceId 指定的活动实例ID
	 */
	void terminateActivityInstance(long activityInstanceId);
	
	/**
	 * 强制终止指定的活动实例，并且指定里路程启动指定的活动定义。<br>
	 * {@link #terminateActivityInstance(long)}方法会按照流程定义运行，而此方法用于实现退回或跳转需求<br>
	 * @param activityInstanceId 指定的活动实例
	 * @param targetActivityDefinitionId 指定要启动的下一步活动定义。
	 */
	void terminateActivityInstance(long activityInstanceId,String targetActivityDefinitionId);
	
	/**
	 * 强制终止流程实例，流程实例以及其下的活动实例、任务项实例以及流程变量数据将会转移到历史库中。
	 * @param processInstanceId 指定的流程实例ID。
	 */
	void terminateProcessInstance(long processInstanceId);
	
	/**
	 * 挂起指定的流程实例。只有处于活跃状态的流程实例才能被挂起，否则引擎抛出异常<br>
	 * 流程挂起后，其状态变换为{@link org.runbpm.entity.EntityConstants.PROCESS_STATE#SUSPENDED}
	 * @param processInstanceId 指定的流程实例ID。
	 */
	void suspendProcessInstance(long processInstanceId);

	/**
	 * 恢复指定的流程实例。只有处于挂起状态的流程实例才能被挂起，否则引擎抛出异常<br>
	 * 流程挂起后，其状态变换为流程挂起之前的状态，{@link org.runbpm.entity.EntityConstants.PROCESS_STATE#NOT_STARTED}和{@link org.runbpm.entity.EntityConstants.PROCESS_STATE#RUNNING}中的一种。
	 * @param processInstanceId 指定的流程实例ID。
	 */
	void resumeProcessInstance(long processInstanceId);
	

	/**
	 * 挂起指定的活动实例。只有处于活跃状态的活动实例才能被挂起，否则引擎抛出异常<br>
	 * 活动挂起后，其状态变换为{@link org.runbpm.entity.EntityConstants.ACTIVITY_STATE#SUSPENDED}
	 * @param activityInstanceId 指定的活动实例ID。
	 */
	void suspendActivityInstance(long activityInstanceId);
	

	/**
	 * 恢复指定的活动实例。只有处于挂起状态的活动实例才能被挂起，否则引擎抛出异常<br>
	 * 活动挂起后，其状态变换为流程挂起之前的状态，{@link org.runbpm.entity.EntityConstants.ACTIVITY_STATE#NOT_STARTED}和{@link org.runbpm.entity.EntityConstants.ACTIVITY_STATE#RUNNING}中的一种。
	 * @param activityInstanceId 指定的活动实例ID。
	 */
	void resumeActivityInstance(long activityInstanceId);
	
	
	/**
	 * 如果一个用户活动，也就是UerTask为抢任务模式（或者成为单一执行模式），可通过此方法获取任务的执行权，其他人之后将无法获取该任务。<br>
	 * 该方法不适用于会签模式。
	 * @param userTaskId 指定的任务实例ID。
	 */
	void claimUserTask(long userTaskId);
	
	/**
	 * 完成某一项任务。完成任务后对于流程驱动的影响：<br>
	 * 如果一个用户活动，也就是UerTask为抢任务模式（或者成为单一执行模式），执行此方法后引擎自动触发 {@link #completeActivityInstance(long)}方法。<br>
	 * 如果一个用户活动，也就是UerTask为会签模式，在最后一个人执行后，引擎自动触发 {@link #completeActivityInstance(long)}方法 。<br>
	 * @param userTaskId 指定的任务实例ID。
	 */
	void completeUserTask(long userTaskId);
	
	/**
	 * 终止某一任务，与 {@link #completeUserTask(long)} 唯一的不同是设置在UserTask表记录里的状态位不一样。
	 * 终止后对于流程驱动的影响参照 {@link #completeUserTask(long)}
	 * @param userTaskId 指定的任务实例ID。
	 */
	void terminateUserTask(long userTaskId);
	
	/**
	 * 挂起指定的任务实例。只有处于活跃状态的任务实例才能被挂起，否则引擎抛出异常<br>
	 * 任务挂起后，其状态变换为{@link org.runbpm.entity.EntityConstants.TASK_STATE#SUSPENDED}
	 * @param userTaskId 指定的任务实例ID。
	 */
	void suspendUserTask(long userTaskId);
	

	/**
	 * 恢复指定的任务实例。只有处于挂起状态的任务实例才能被挂起，否则引擎抛出异常<br>
	 * 任务挂起后，其状态变换为流程挂起之前的状态，{@link org.runbpm.entity.EntityConstants.TASK_STATE#NOT_STARTED}和{@link org.runbpm.entity.EntityConstants.TASK_STATE#RUNNING}中的一种。
	 * @param userTaskId 指定的任务实例ID。
	 */
	void resumeUserTask(long userTaskId);
	
	/**
	 * 取消一个任务。该方法只会将任务状态置为取消，不会驱动流程往下执行。如果需要将此任务删除，请使用{@ #removeUserTask(long)}方法
	 * @param userTaskId 需要取消的任务ID
	 */
	void cancelUserTask(long userTaskId);
	
	/**
	 * 删除一个任务。如果需要保留该任务同时置为取消，请使用{@ #cancelUserTask(long)}方法
	 * @param userTaskId 需要删除的任务ID
	 */
	void removeUserTask(long userTaskId);
	
	
	/**
	 * 将此任务重新分配一个执行人。该方法会将任务直接执行人。<br>
	 * 如果需要保留原执行人的任务，可综合 {@ link #cancelUserTask(long)} 和{@ link #addUserTask(long, long)}两个方法实现。 
	 * @param userTaskId 指定的任务ID
	 * @param userId 执行人ID
	 */
	void setAssignee(long userTaskId,String userId);
	
	/**
	 * 动态增加一个任务
	 * @param activityInstanceID 对应的活动实例。该活动定义必须是人工活动，否则会抛出异常。
	 * @param userId 执行人ID
	 * @param state 添加任务后的状态，如果是会签模式，应该选择{@link EntityConstants.TASK_STATE#RUNNING};如果是竞争(抢任务）模式，则应该选择{@link EntityConstants.TASK_STATE#NOT_STARTED}
	 * @return 所创建的活动实例
	 * 
	 */
	TaskInstance addUserTask(long activityInstanceId,String userId,EntityConstants.TASK_STATE state);
	
	/**
	 * 如果一个用户活动，也就是UerTask为抢任务模式（或者成为单一执行模式），在通过 {@link #claimUserTask(long)}获取任务的执行权后，可以通过此方法将任务重新放回，所有的人可以重新抢任务。<br>
	 * 如果一个用户活动，也就是UerTask为会签模式，不适用执行该方法。<br>
	 * @param userTaskId 指定的任务实例ID。
	 */
	void putbackUserTask(long userTaskId);
	
	/**
	 * 如果一个用户活动，也就是UerTask为抢任务模式（或者成为单一执行模式），在通过 {@link #claimUserTask(long)}获取任务的执行权后，可以通过此方法将任务重新分配给另一个人。<br>
	 * 如果一个用户活动，也就是UerTask为会签模式，也可以通过此方法将任务重新分配给另一个人。<br>
	 * @param userTaskId 指定的任务实例ID。
	 * @param userId 用户ID。
	 */
	void setUserTaskAssignee(long userTaskId,String userId) ;
	
	/**
	 * 获取指定的流程实例
	 * @param processInstanceId 指定的流程实例ID。
	 * @return 流程实例对象
	 */
	ProcessInstance loadProcessInstance(long processInstanceId);
	
	/**
	 * 获取指定的活动实例
	 * @param activityInstanceId 指定的活动实例ID。
	 * @return 流程实例对象
	 */
	ActivityInstance loadActivityInstance(long activityInstanceId);
	
	/**
	 * 获取指定的工作项实例
	 * @param taskInstanceId 指定的工作项实例ID。
	 * @return 工作项实例对象
	 */
	TaskInstance loadTaskInstance(long taskInstanceId);

	/**
	 * 获取指定流程实例下的所有活动实例列表。
	 * @param processInstId 指定的流程实例ID。
	 * @return 活动实例对象
	 */
	List<ActivityInstance>  listActivityInstanceByProcessInstId(long processInstId);

	
	/**
	 * 获取指定流程实例，符合指定活动状态的活动实例列表。
	 * @param processInstanceId 指定的流程实例ID。
	 * @param stateSet 活动状态常量列表
	 * 举例：
	 * <pre>
		EnumSet<ACTIVITY_STATE> set = EnumSet.noneOf(ACTIVITY_STATE.class);  
		set.add(ACTIVITY_STATE.NOT_STARTED);  
		set.add(ACTIVITY_STATE.RUNNING);
		set.add(ACTIVITY_STATE.SUSPENDED);
		</pre>
	 * @return 活动实例列表
	 */
	List<ActivityInstance> listActivityInstanceByProcessInstIdAndState(long processInstanceId,EnumSet<ACTIVITY_STATE> stateSet);
	/**
	 * 获取指定流程实例下，指定的SubProcess下的，指定的活动定义的活动实例列表。<br>
	 * @param processInstanceId 指定的流程实例ID。
	 * @param subProcessId SubProcessId 块活动在XPDL规范里为ActivitySet，在BPMN规范里为SubProcess。
	 * @param stateSet 活动状态常量列表
	 * 举例：
	 * <pre>
		EnumSet<ACTIVITY_STATE> set = EnumSet.noneOf(ACTIVITY_STATE.class);  
		set.add(ACTIVITY_STATE.NOT_STARTED);  
		set.add(ACTIVITY_STATE.RUNNING);
		set.add(ACTIVITY_STATE.SUSPENDED);
		</pre>
	 * @return 活动实例列表
	 */
	List<ActivityInstance> listActivityInstanceByProcessInstIdSubrocessIdAndState(long processInstanceId,String subProcessId,EnumSet<ACTIVITY_STATE> stateSet);
	
	/**
	 * 获取指定流程实例下，指定的活动定义的活动实例列表。
	 * @param processInstanceId 指定的流程实例ID。
	 * @param activityDefinitionId 指定的活动定义ID。
	 * @return 活动实例列表
	 */
	List<ActivityInstance>  listActivityInstanceByActivityDefId(long processInstanceId,String activityDefinitionId);
	/**
	 * 获取指定流程实例下，指定的活动定义，指定的活动状态的活动实例列表。
	 * 
	 * @param processInstanceId 指定的流程实例ID。
	 * @param activityDefinitionId 指定的活动定义ID。
	 * @param stateSet 活动状态常量列表
	 * 举例：
	 * <pre>
		EnumSet<ACTIVITY_STATE> set = EnumSet.noneOf(ACTIVITY_STATE.class);  
		set.add(ACTIVITY_STATE.NOT_STARTED);  
		set.add(ACTIVITY_STATE.RUNNING);
		set.add(ACTIVITY_STATE.SUSPENDED);
		</pre>
	 * @return 活动实例列表
	 */
	List<ActivityInstance> listActivityInstanceByActivityDefIdAndState(long processInstanceId,String activityDefinitionId,EnumSet<ACTIVITY_STATE> stateSet);
	
	/**
	 * 获取指定活动实例下的所有任务项列表。
	 * @param activityInstanceId 指定的活动实例ID
	 * @return 任务对象列表
	 */
	List<TaskInstance> listTaskInstanceByActivityInstId(long activityInstanceId);
	
	/**
	 * 获取指定流程实例下的的所有任务项列表。
	 * @param processInstanceId 指定流程
	 * @return 任务项列表
	 */
	List<TaskInstance> listTaskInstanceByProcessInstId(long processInstanceId);
	
	/**
	 * 获取指定用户的，指定活动实例的，且符合指定状态的任务项列表。<br>
	 * @param activityInstanceId 指定的活动实例ID
	 * @param stateSet 该参数的输入样例，可参照： {@link #listTaskInstanceByUserIdAndState(String, EnumSet)}
	 * @return 任务项列表
	 */
	List<TaskInstance> listTaskInstanceByActivityInstIdAndState(long activityInstanceId,EnumSet<TASK_STATE> stateSet);
	
	/**
	 * 获取指定用户的，指定状态的任务项列表。使用该API获取的工作项所在的流程都在运行之中，还没有转移到历史库中。
	 * 
	 * @param userId 指定的用户状态
	 * @param stateSet 指定的状态 <br>
	 * 例1：获取所有活跃状态的任务的输入参数
	  		<pre>
			EnumSet<EntityConstants.TASK_STATE> stateSet = EnumSet.noneOf(EntityConstants.TASK_STATE.class);  
			stateSet.add(EntityConstants.TASK_STATE.NOT_STARTED);  
			stateSet.add(EntityConstants.TASK_STATE.RUNNING);
            </pre>
	 * 例2：获取所有结束状态的任务的输入参数<br>
	 		<pre>
	 		EnumSet<EntityConstants.TASK_STATE> stateSet = EnumSet.noneOf(EntityConstants.TASK_STATE.class); 
	 		stateSet.add(EntityConstants.TASK_STATE.TERMINATED);
	 		stateSet.add(EntityConstants.TASK_STATE.COMPLETED);
            </pre>
	 * 例3：获取处于挂起状态的任务的输入参数<br>
			<pre>
			EnumSet<EntityConstants.TASK_STATE> stateSet = EnumSet.noneOf(EntityConstants.TASK_STATE.class);  
			stateSet.add(EntityConstants.TASK_STATE.SUSPENDED);
			</pre>
	 * @return 任务项列表
	 */
	List<TaskInstance> listTaskInstanceByUserIdAndState(String userId,EnumSet<TASK_STATE> stateSet);
	
	/**
	 * 获取指定用户的所有的任务项。
	 * @param userId 用户ID
	 * @return 任务项列表
	 */
	List<TaskInstance> listTaskInstanceByUserId(String userId) ;
	


	/**
	 * 为指定的流程实例，设置一对流程变量
	 * @param processInstanceId 指定的流程实例ID
	 * @param name 名称
	 * @param value 值
	 */
	void setProcessVariable(long processInstanceId, String name, Object value);
	
	/**
	 * 为指定的流程实例，设置多对流程变量。
	 * @param processInstanceId 指定的流程实例ID
	 * @param dataFieldMap 多对流程变量
	 */
	void setProcessVariableMap(long processInstanceId, Map<String, Object>dataFieldMap);
	
	/**
	 * 获取指定流程的的流程变量列表。
	 * @param processInstanceId
	 * @return 最终的变量值，使用 {@link org.runbpm.entity.VariableInstance#getValue()}获得
	 */
	Map<String,VariableInstance> loadVariableMap(long processInstanceId);
	
	/**
	 * 查询根据指定创建者，所创建的流程实例列表。<br>
	 * 流程实例的创建者字段，根据创建流程API时的输入赋值。
	 * @param creator 创建者
	 * @return 流程实例列表
	 */
	List<ProcessInstance> listProcessInstanceByCreator(String creator);
	
	/**
	 * 查询根据指定创建者，所创建的流程历史列表。<br>
	 * 流程实例的创建者字段，根据创建流程API时的输入赋值。
	 * @param creator 创建者
	 * @return 流程历史列表
	 */
	List<ProcessHistory> listProcessHistoryByCreator(String creator);
	
	/**
	 * 获取指定的流程历史
	 * @param processHistoryId
	 * @return 流程历史对象
	 */
	ProcessHistory loadProcessHistory(long processHistoryId);
	
	/**
	 * 获取指定的活动历史
	 * @param activityHistoryId 活动历史ID
	 * @return 活动历史对象
	 */
	ActivityHistory loadActivityHistory(long activityHistoryId);
	
	/**
	 * 获取任务对象历史
	 * @param taskHistoryId 任务历史对象ID 
	 * @return 任务历史对象
	 */
	TaskHistory loadTaskHistory(long taskHistoryId);
	
	/**
	 * 获取指定流程历史下的所有活动对象列表
	 * @param processHistoryId 指定的流程历史ID
	 * @return 活动历史对象列表
	 */
	List<ActivityHistory> listActivityHistoryByProcessInstId(long processHistoryId);
	
	/**
	 * 加载指定流程历史记录下的，指定活动定义的活动实例历史 
	 * @param processHistoryId 流程历史记录
	 * @param activityDefinitionId 活动定义
	 * @return 指定的活动实例历史
	 */
	List<ActivityHistory> listActivityHistoryByActivityDefId(long processHistoryId, String activityDefinitionId);
	
	/**
	 * 加载指定流程历史下的，所有任务历史列表
	 * @param processHistoryId 流程历史ID
	 * @return 任务历史列表
	 */
	List<TaskHistory> listTaskHistoryByProcessInstId(long processHistoryId);
	
	/**
	 * 加载指定活动历史下的，所有任务历史列表
	 * @param activityHistoryId 活动历史ID
	 * @return 任务历史列表
	 */
	List<TaskHistory> listTaskHistoryByActivityInstId(long activityHistoryId);
	
	
	/**
	 * 在某一个流程运行时，获取当前活动实例下一步可能到达的活动定义。流程引擎在计算时，会考虑流程上下文变量。
	 * 
	 * 如果需要在流程定义层面，获取指定的活动定义未来可能到达的所有活动定义（不考虑流程上下文变量，用于流程跳转等场景），参考{@link org.runbpm.bpmn.definition.ProcessDefinition#listReachableActivitySet(ActivityDefinition)}
	 * @param activityInstanceId 活动实例ID
	 * @return 可以到达的活动定义
	 */
	Set<ActivityDefinition> listReachableActivitySet(long activityInstanceId);
	
	
	/**
	 * 根据指定的流程实例，以及该流程实例的活动定义，通过上下文判断，该任务分配定义可以输出的执行人
	 * @param processInstanceId 指定的流程实例
	 * @param activityDefinitionId 该流程实例的活动定义
	 * @return 通过上下文判断，该任务分配定义可以输出的执行人
	 */
	List<User> evalUserList(long processInstanceId,String activityDefinitionId);

	/**
	 * 获得流程引擎的流程模板列表，如果一个流程定义被导入多次，则会返回多条记录。
	 * * <ul>
	 * 		<li>
	 * 		如果是第一次调用此方法（例如当初始化流程引擎时），而且流程引擎使用的是持久化存储，例如数据库或者NoSQL,使用该方法会读取存放在持久化存储中的XML字段，通过JAXB序列化为流程定义对象。<br>
	 * 		初始化流程引擎的Web方式启动插件参照：{@link org.runbpm.utils.InitRunBPMSpringContextServlet()}
	 * 		</li>
	 * 		<li>
	 * 		初始化完毕后，每一次流程导入都会同步流程引擎的模板内存。每次此方法只会读取流程引擎内存的模板。
	 * 		</li>
	 * </ul>
	 * @return 流程模板列表
	 */
	List<ProcessModel> loadProcessModelsWithAllVersions();	
	
}
