package org.runbpm.exception;


/**
 * 链式异常功能
 * throw new RunBPMException("");
 */
public class RunBPMException extends RuntimeException {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 684278639408081728L;

	private EXCEPTION_MESSAGE exception_message;
	
	//00 definition
	//01 instance发现定义错误
	//02 instance引擎
	//03 
	public enum EXCEPTION_MESSAGE {
		
		Code_000000_NO_INIT_RunBPM("无法加载Spring内部变量，RumBPM没有被正确的初始化Spring ApplicationContext"),
		Code_000001_INVALID_SPRING_APPLICATIONCONTEXT("非法的Spring ApplicationContext变量"),
		Code_999999_NO_RunBPM_Rollback("RunBPM异常"),
		
		Code_000004_Invalid_Subflow_Execution_Type("子流程执行模式既不是同步，也不是异步；流程定义有问题。"),
		Code_000005_Mismatching_Formal_and_Actual_Parameter_SIZE("子流程的形参和子流程活动的实参个数不匹配"),
		Code_000006_Null_ActivitySet_By_BlockId("无法根据指定的blockId找到活动集定义"),
		Code_000007_Empty_Definition_Block("流程定义不完整"),
		
		Code_100001_Invalid_ActityDefinition_Type("通过活动定义类型无法获取指定的活动容器"),
		Code_100002_Invalid_ProcessModel("通过流程定义ID无法获取的ProcessModel."),
		
		Code_020000_NoResult_For_Such_DefinitionId("流程定义库中没有通过指定的流程定义"),
		Code_020001_Empty_Activity("该活动节点后，无法路由到下一个节点，可能是工作流变量赋值不对，满足不了连接弧线的表达式的转移条件。"),
		Code_020002_Invalid_Result_From_EVAL("连接弧表达式计算的结果不是Boolean类型，将无法做连接转移判断，请注意工作流变量以及表达式定义。"),
		Code_020003_Runtime_Exception_From_Aviator("计算连接弧表达式时，Aviator出现异常"),
		Code_020003_Runtime_Exception_From_JUEL("连接弧底层计算表达式时出错，注意检查工作流变量是否赋值,工作流变量类型是否匹配，例如字符类型用来做比较，以及连接弧转移条件的定义等。"),
		Code_020004_Null_DataField_Definition("流程定义中没有要指定的DataField ID"),
		Code_020005_No_Formal_Parameter_Input("模式为IN或者INPUT的流程变量没有被赋值，该流程无法启动。"),
		Code_020006_Mismatching_Datafiled_Type_and_Value("不匹配的工作流变量定义和赋值"),
		Code_020007_Cannot_Claim_Task_for_Invalid_State("不能够Claim任务，因为状态不对。"),
		Code_020008_Cannot_Complete_Task_for_Invalid_State("不能够Complete任务，因为状态不对。"),
		Code_020009_GOT_NULL_VAR_FOR_Variable("创建工作时，根据获取的流程变量为空，无法找到执行人"),
		Code_020010_CANNOT_FIND_ACTIVITY_BY_DEFINITIONID("根据指定的活动定义ID无法找到活动定义，流程无法启动"),
		Code_020007_Cannot_setAssignee_Task_for_Invalid_State("不能够重新设置执行人，因为状态不是运行状态"),
		Code_020007_Cannot_putback_Task_for_Invalid_State("不能够放回任务，因为状态不是运行状态"),
		Code_020007_Cannot_putback_Task_for_Multi_UserTask("不能够放回任务，因为活动是会签活动"),
		Code_020007_Cannot_addUser_Task_for_NOT_UserTask("不能够增加任务，因为该活动不是人工活动"),
		
		
		Code_020010_INVALID_PROCESSINSTANCE_TO_START("流程不是处于未启动状态，所以不能被启动"),
		Code_020010_INVALID_PROCESSINSTANCE_TO_SUSPEND("流程不是处于未启动或者运行状态，所以不能被挂起"),
		Code_020011_INVALID_ACTIVITYINSTANCE_TO_SUSPEND("活动不是处于未启动或者运行状态，所以不能被挂起"),
		Code_020012_INVALID_TASK_TO_SUSPEND("工作项不是处于未启动或者运行状态，所以不能被挂起"),
		Code_020013_INVALID_PROCESSINSTANCE_TO_RESUME("流程不是处于挂起状态，所以不能执行恢复操作"),
		Code_020014_INVALID_ACTIVITYINSTANCE_TO_RESUME("活动不是处于挂起状态，所以不能执行恢复操作"),
		Code_020015_INVALID_TASKINSTANCE_TO_RESUME("工作项不是处挂起状态，所以不能执行恢复操作"),
		Code_020016_INVALID_ACTIVITYINSTANCE_TO_COMPLETE("活动不是处于活跃状态，所以不能执行完成操作"),
		Code_020017_INVALID_ACTIVITYINSTANCE_TO_TERMINATE("活动不是处于活跃状态，所以不能执行终止操作"),
		Code_020018_INVALID_USERTASK_TO_COMPLETE("工作项不是处于活跃状态，所以不能执行完成操作"),
		Code_020019_INVALID_USERTASK_TO_TERMINATE("工作项不是处于活跃状态，所以不能执行终止操作"),
		Code_020020_WORKITEM_NOT_COMPLETE("尚有未结束的工作项，不能执行完成操作"),
		Code_020021_NON_PROCESSINSTANCE("所涉及的流程实例为空对象，请检查流程ID是否存在"),
		Code_020022_NON_ACTIVITYINSTANCE("所涉及的活动实例为空对象，请检查活动ID是否存在"),
		Code_020023_NON_USERTASK("所涉及的任务实例为空对象，请检查任务ID是否存在"),
		Code_020024_INVALID_ACTIVITYINSTANCE_TO_ADDUSERTASK("活动不是处于未启动或者运行状态，所以不能增加工作项"),
		Code_020025_INVALID_ACTIVITYINSTANCE_TO_ADDUSERTASK("活动已经被人claimed，所以不能增加工作项"),
		
		
		Code_020100_NO_ResourceAssignmentHandler_Impl("根据spring bean id获取的类没有实现资源接口"),
		Code_020101_NO_SpecialConditionHandler_Impl("根据spring bean id获取的类没有实现转移条件接口"),
		Code_020110_CANNT_INIT_resourceAssignmentHandler("不能转换为指定资源接口"),
		Code_020111_CANNT_INIT_SpecialConditionHandler_Impl("不能转换为指定转移条件接口"),
		Code_020102_GOT_NULL_VAR_FOR_SpecialConditionHandler_Impl("根据连接定义的转移条件变量获取为空，没有为改变量赋值"),
		
		Code_020101_NO_ServiceTaskHandler_From_Spring("根据spring bean id获取的类没有实现ServiceTaskHandler接口"),
		Code_020210_CANNT_INIT_ServiceTaskHandler("不能转换为指定ServiceTaskHandler接口"),
		Code_020211_GOT_NULL_VAR_FOR_ServiceTaskHandler("获取ServiceTaskHandler接口，根据获取的流程变量为空，无法找到"),
		
		Code_020301_UNEXPECTED_LISTENER("不是期望的监听类型"),
		Code_020302_INVOKE_LISTENER_EXCEPTION("调用监听器出错"),
		Code_020303_INVALID_LISTENER_CLASSNAME("指定类名不能转换为监听器"),
		Code_020304_INVALID_LISTENER_TYPE("输入的监听器类型不在支持范围内"),
		
		//Code_020100_CANNT_INIT_resourceAssignmentHandler("不能转换为指定资源接口"),
		
		Code("//TODO");
		
		private String exceptionMessage;
		
		private EXCEPTION_MESSAGE(String exceptionMessage ){
			this.exceptionMessage=exceptionMessage;
		}
		
		public String getExceptionMessage() {
			return exceptionMessage;
		}
		
	}

	private static final String CAUSED_BY = "\nCaused by: ";

	private Throwable cause = null;

	public RunBPMException(EXCEPTION_MESSAGE exception_message,String extraMessage) {
		super(exception_message+":"+exception_message.exceptionMessage+":"+extraMessage);
		this.exception_message = exception_message;
	}
	
	
	public RunBPMException(EXCEPTION_MESSAGE exception_message,String extraMessage,Throwable cause) {
		super(exception_message+":"+exception_message.exceptionMessage+":"+extraMessage);
		this.exception_message = exception_message;
		this.cause=cause;
	}

	public RunBPMException(EXCEPTION_MESSAGE exception_message) {
		super(exception_message+":"+exception_message.exceptionMessage);
		this.exception_message = exception_message;
	}


	public RunBPMException(EXCEPTION_MESSAGE exception_message,Throwable cause) {
		super(exception_message+":"+exception_message.exceptionMessage);
		this.exception_message = exception_message;
		this.cause=cause;
	}
	

	public RunBPMException(String internelErrorMessage) {
		super("RunBPM Internel error:"+internelErrorMessage);
		
	}

	/**
	 * 使用下层异常作为构造函数
	 * 
	 * @param cause
	 *            嵌套异常(caused by)
	 */
	public RunBPMException(Throwable cause) {
		super();
		this.cause = cause;
	}

	

	public RunBPMException(String internelErrorMessage,Throwable cause) {
		super(internelErrorMessage,cause);
		this.cause = cause;
	}

	/**
	 * 获取导致该异常的下层异常，或者为空
	 * 
	 * @return 导致该异常的下层异常
	 */
	public Throwable getCause() {
		return cause;
	}

	/**
	 * 将异常使用字符串表示
	 * 
	 * @return 使用字符串表示异常
	 */
	public String toString() {
		if (cause == null) {
			return super.toString();
		} else {
			return super.toString() + CAUSED_BY + cause.toString();
		}
	}

	/**
	 * 将异常堆栈输出，如使用System.err (输出将包括根异常,或为空)
	 */
	public void printStackTrace() {
		super.printStackTrace();
		if (cause != null) {
			System.err.println(CAUSED_BY);
			cause.printStackTrace();
		}
	}

	/**
	 * 将异常信息输出至PrintStream (包括跟异常,或者为空)
	 * 
	 * @param ps -
	 *            要输出的PrintStream
	 */
	public void printStackTrace(java.io.PrintStream ps) {
		super.printStackTrace(ps);
		if (cause != null) {
			ps.println(CAUSED_BY);
			cause.printStackTrace(ps);
		}
	}

	/**
	 * 将异常信息输出至PrintWriter (包括跟异常,或者为空)
	 * 
	 * @param pw -
	 *            要输出的PrintWriter
	 */
	public void printStackTrace(java.io.PrintWriter pw) {
		super.printStackTrace(pw);
		if (cause != null) {
			pw.println(CAUSED_BY);
			cause.printStackTrace(pw);
		}
	}
	
	
	public EXCEPTION_MESSAGE getException_message() {
		return exception_message;
	}
	
}
