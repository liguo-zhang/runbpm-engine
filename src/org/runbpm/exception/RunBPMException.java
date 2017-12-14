package org.runbpm.exception;


/**
 * ��ʽ�쳣����
 * throw new RunBPMException("");
 */
public class RunBPMException extends RuntimeException {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 684278639408081728L;

	private EXCEPTION_MESSAGE exception_message;
	
	//00 definition
	//01 instance���ֶ������
	//02 instance����
	//03 
	public enum EXCEPTION_MESSAGE {
		
		Code_000000_NO_INIT_RunBPM("�޷�����Spring�ڲ�������RumBPMû�б���ȷ�ĳ�ʼ��Spring ApplicationContext"),
		Code_000001_INVALID_SPRING_APPLICATIONCONTEXT("�Ƿ���Spring ApplicationContext����"),
		Code_999999_NO_RunBPM_Rollback("RunBPM�쳣"),
		
		Code_000004_Invalid_Subflow_Execution_Type("������ִ��ģʽ�Ȳ���ͬ����Ҳ�����첽�����̶��������⡣"),
		Code_000005_Mismatching_Formal_and_Actual_Parameter_SIZE("�����̵��βκ������̻��ʵ�θ�����ƥ��"),
		Code_000006_Null_ActivitySet_By_BlockId("�޷�����ָ����blockId�ҵ��������"),
		Code_000007_Empty_Definition_Block("���̶��岻����"),
		
		Code_100001_Invalid_ActityDefinition_Type("ͨ������������޷���ȡָ���Ļ����"),
		Code_100002_Invalid_ProcessModel("ͨ�����̶���ID�޷���ȡ��ProcessModel."),
		
		Code_020000_NoResult_For_Such_DefinitionId("���̶������û��ͨ��ָ�������̶���"),
		Code_020001_Empty_Activity("�û�ڵ���޷�·�ɵ���һ���ڵ㣬�����ǹ�����������ֵ���ԣ����㲻�����ӻ��ߵı��ʽ��ת��������"),
		Code_020002_Invalid_Result_From_EVAL("���ӻ����ʽ����Ľ������Boolean���ͣ����޷�������ת���жϣ���ע�⹤���������Լ����ʽ���塣"),
		Code_020003_Runtime_Exception_From_Aviator("�������ӻ����ʽʱ��Aviator�����쳣"),
		Code_020003_Runtime_Exception_From_JUEL("���ӻ��ײ������ʽʱ����ע���鹤���������Ƿ�ֵ,���������������Ƿ�ƥ�䣬�����ַ������������Ƚϣ��Լ����ӻ�ת�������Ķ���ȡ�"),
		Code_020004_Null_DataField_Definition("���̶�����û��Ҫָ����DataField ID"),
		Code_020005_No_Formal_Parameter_Input("ģʽΪIN����INPUT�����̱���û�б���ֵ���������޷�������"),
		Code_020006_Mismatching_Datafiled_Type_and_Value("��ƥ��Ĺ�������������͸�ֵ"),
		Code_020007_Cannot_Claim_Task_for_Invalid_State("���ܹ�Claim������Ϊ״̬���ԡ�"),
		Code_020008_Cannot_Complete_Task_for_Invalid_State("���ܹ�Complete������Ϊ״̬���ԡ�"),
		Code_020009_GOT_NULL_VAR_FOR_Variable("��������ʱ�����ݻ�ȡ�����̱���Ϊ�գ��޷��ҵ�ִ����"),
		Code_020010_CANNOT_FIND_ACTIVITY_BY_DEFINITIONID("����ָ���Ļ����ID�޷��ҵ�����壬�����޷�����"),
		Code_020007_Cannot_setAssignee_Task_for_Invalid_State("���ܹ���������ִ���ˣ���Ϊ״̬��������״̬"),
		Code_020007_Cannot_putback_Task_for_Invalid_State("���ܹ��Ż�������Ϊ״̬��������״̬"),
		Code_020007_Cannot_putback_Task_for_Multi_UserTask("���ܹ��Ż�������Ϊ��ǻ�ǩ�"),
		Code_020007_Cannot_addUser_Task_for_NOT_UserTask("���ܹ�����������Ϊ�û�����˹��"),
		
		
		Code_020010_INVALID_PROCESSINSTANCE_TO_START("���̲��Ǵ���δ����״̬�����Բ��ܱ�����"),
		Code_020010_INVALID_PROCESSINSTANCE_TO_SUSPEND("���̲��Ǵ���δ������������״̬�����Բ��ܱ�����"),
		Code_020011_INVALID_ACTIVITYINSTANCE_TO_SUSPEND("����Ǵ���δ������������״̬�����Բ��ܱ�����"),
		Code_020012_INVALID_TASK_TO_SUSPEND("������Ǵ���δ������������״̬�����Բ��ܱ�����"),
		Code_020013_INVALID_PROCESSINSTANCE_TO_RESUME("���̲��Ǵ��ڹ���״̬�����Բ���ִ�лָ�����"),
		Code_020014_INVALID_ACTIVITYINSTANCE_TO_RESUME("����Ǵ��ڹ���״̬�����Բ���ִ�лָ�����"),
		Code_020015_INVALID_TASKINSTANCE_TO_RESUME("������Ǵ�����״̬�����Բ���ִ�лָ�����"),
		Code_020016_INVALID_ACTIVITYINSTANCE_TO_COMPLETE("����Ǵ��ڻ�Ծ״̬�����Բ���ִ����ɲ���"),
		Code_020017_INVALID_ACTIVITYINSTANCE_TO_TERMINATE("����Ǵ��ڻ�Ծ״̬�����Բ���ִ����ֹ����"),
		Code_020018_INVALID_USERTASK_TO_COMPLETE("������Ǵ��ڻ�Ծ״̬�����Բ���ִ����ɲ���"),
		Code_020019_INVALID_USERTASK_TO_TERMINATE("������Ǵ��ڻ�Ծ״̬�����Բ���ִ����ֹ����"),
		Code_020020_WORKITEM_NOT_COMPLETE("����δ�����Ĺ��������ִ����ɲ���"),
		Code_020021_NON_PROCESSINSTANCE("���漰������ʵ��Ϊ�ն�����������ID�Ƿ����"),
		Code_020022_NON_ACTIVITYINSTANCE("���漰�Ļʵ��Ϊ�ն�������ID�Ƿ����"),
		Code_020023_NON_USERTASK("���漰������ʵ��Ϊ�ն�����������ID�Ƿ����"),
		Code_020024_INVALID_ACTIVITYINSTANCE_TO_ADDUSERTASK("����Ǵ���δ������������״̬�����Բ������ӹ�����"),
		Code_020025_INVALID_ACTIVITYINSTANCE_TO_ADDUSERTASK("��Ѿ�����claimed�����Բ������ӹ�����"),
		
		
		Code_020100_NO_ResourceAssignmentHandler_Impl("����spring bean id��ȡ����û��ʵ����Դ�ӿ�"),
		Code_020101_NO_SpecialConditionHandler_Impl("����spring bean id��ȡ����û��ʵ��ת�������ӿ�"),
		Code_020110_CANNT_INIT_resourceAssignmentHandler("����ת��Ϊָ����Դ�ӿ�"),
		Code_020111_CANNT_INIT_SpecialConditionHandler_Impl("����ת��Ϊָ��ת�������ӿ�"),
		Code_020102_GOT_NULL_VAR_FOR_SpecialConditionHandler_Impl("�������Ӷ����ת������������ȡΪ�գ�û��Ϊ�ı�����ֵ"),
		
		Code_020101_NO_ServiceTaskHandler_From_Spring("����spring bean id��ȡ����û��ʵ��ServiceTaskHandler�ӿ�"),
		Code_020210_CANNT_INIT_ServiceTaskHandler("����ת��Ϊָ��ServiceTaskHandler�ӿ�"),
		Code_020211_GOT_NULL_VAR_FOR_ServiceTaskHandler("��ȡServiceTaskHandler�ӿڣ����ݻ�ȡ�����̱���Ϊ�գ��޷��ҵ�"),
		
		Code_020301_UNEXPECTED_LISTENER("���������ļ�������"),
		Code_020302_INVOKE_LISTENER_EXCEPTION("���ü���������"),
		Code_020303_INVALID_LISTENER_CLASSNAME("ָ����������ת��Ϊ������"),
		Code_020304_INVALID_LISTENER_TYPE("����ļ��������Ͳ���֧�ַ�Χ��"),
		
		//Code_020100_CANNT_INIT_resourceAssignmentHandler("����ת��Ϊָ����Դ�ӿ�"),
		
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
	 * ʹ���²��쳣��Ϊ���캯��
	 * 
	 * @param cause
	 *            Ƕ���쳣(caused by)
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
	 * ��ȡ���¸��쳣���²��쳣������Ϊ��
	 * 
	 * @return ���¸��쳣���²��쳣
	 */
	public Throwable getCause() {
		return cause;
	}

	/**
	 * ���쳣ʹ���ַ�����ʾ
	 * 
	 * @return ʹ���ַ�����ʾ�쳣
	 */
	public String toString() {
		if (cause == null) {
			return super.toString();
		} else {
			return super.toString() + CAUSED_BY + cause.toString();
		}
	}

	/**
	 * ���쳣��ջ�������ʹ��System.err (������������쳣,��Ϊ��)
	 */
	public void printStackTrace() {
		super.printStackTrace();
		if (cause != null) {
			System.err.println(CAUSED_BY);
			cause.printStackTrace();
		}
	}

	/**
	 * ���쳣��Ϣ�����PrintStream (�������쳣,����Ϊ��)
	 * 
	 * @param ps -
	 *            Ҫ�����PrintStream
	 */
	public void printStackTrace(java.io.PrintStream ps) {
		super.printStackTrace(ps);
		if (cause != null) {
			ps.println(CAUSED_BY);
			cause.printStackTrace(ps);
		}
	}

	/**
	 * ���쳣��Ϣ�����PrintWriter (�������쳣,����Ϊ��)
	 * 
	 * @param pw -
	 *            Ҫ�����PrintWriter
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
