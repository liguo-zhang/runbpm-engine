package org.runbpm.entity;

import java.util.Date;

 interface EntityInterface {
	
	 /**
	  * ��ȡ����ʵ�����ʵ������������ʵ����ID��
	 * @return
	 */
	long getId();
	
	 /**
	  * �÷���Ϊ�����������ڲ�����
	 * @param id
	 */
	void setId(long id);
	
	 /**
	  * ��ȡ����ʵ�����ʵ������������ʵ�������ơ�
	 * @return
	 */
	String getName() ;

	 /**
	  * �÷���Ϊ�����������ڲ�����
	 * @param name
	 */
	void setName(String name) ;

	 /**
	  * ��ȡ����ʵ�����ʵ������������ʵ���Ĵ���ʱ��
	 * @return
	 */
	Date getCreateDate() ;

	 /**
	  * �÷���Ϊ�����������ڲ�����
	 * @param createDate
	 */
	void setCreateDate(Date createDate) ;

	 /**
	  * ��ȡ����ʵ�����ʵ������������ʵ����һ��״̬�����ı������
	 * @return
	 */
	Date getModifyDate() ;

	 /**
	  * �÷���Ϊ�����������ڲ�����
	 * @param modifyDate
	 */
	void setModifyDate(Date modifyDate) ;
}
