package org.runbpm.spring.persistence;

import java.io.IOException;

import junit.framework.Assert;

import org.runbpm.context.Configuration;
import org.runbpm.entity.ActivityInstance;
import org.runbpm.entity.ProcessInstance;
import org.runbpm.entity.EntityConstants.ACTIVITY_STATE;
import org.runbpm.entity.EntityConstants.PROCESS_STATE;
import org.runbpm.exception.RunBPMException;
import org.runbpm.service.RuntimeService;
import org.springframework.core.io.ClassPathResource;

public class PersistenceTest {

	public static void test() throws Exception{
		RuntimeService runtimeService = Configuration.getContext().getRuntimeService();

		String fileName = "PersistenceTest.xml";
		ClassPathResource classPathResource = new ClassPathResource(fileName,PersistenceTest.class);
		runtimeService.initProcessDefinitionFromFile(classPathResource.getFile());
		
		ProcessInstance processInstance = runtimeService.createProcessInstance("PersistenceTest.xml",null);
		
		// 内存形式总是1
		long processInstanceId = processInstance.getId();
		ProcessInstance newProcessInstance = runtimeService.getProcessInstance(processInstanceId);
		Assert.assertNotNull(newProcessInstance.getCreateDate());
		Assert.assertNotNull(newProcessInstance.getModifyDate());
		//Assert.assertEquals("" , newProcessInstance.getProcessModelId(),1);
		Assert.assertEquals("" , newProcessInstance.getState(),PROCESS_STATE.NOT_STARTED);
		
		// 开始节点1结束后，   有 theStart（完成状态） fork（完成状态） receivePayment(运行状态) shipOrder（运行状态） 
		runtimeService.startProcessInstance(processInstanceId);
		newProcessInstance = runtimeService.getProcessInstance(processInstanceId);
		
		Assert.assertEquals("" , newProcessInstance.getState(),PROCESS_STATE.RUNNING);
		Assert.assertEquals("" , runtimeService.getActivityInstanceByProcessInstId(processInstance.getId()).size(),4);
		ActivityInstance activityInstance_Instance_1_0 = runtimeService.getActivityInstanceByActivityDefId(processInstanceId, "theStart").iterator().next();
		ActivityInstance activityInstance_Instance_2_0 = runtimeService.getActivityInstanceByActivityDefId(processInstanceId, "fork").iterator().next();
		ActivityInstance activityInstance_Instance_3_0 = runtimeService.getActivityInstanceByActivityDefId(processInstanceId, "receivePayment").iterator().next();
		ActivityInstance activityInstance_Instance_4_0 = runtimeService.getActivityInstanceByActivityDefId(processInstanceId, "shipOrder").iterator().next();
		
		Assert.assertEquals("" , activityInstance_Instance_1_0.getState(),ACTIVITY_STATE.COMPLETED);
		Assert.assertEquals("" , activityInstance_Instance_2_0.getState(),ACTIVITY_STATE.COMPLETED);
		Assert.assertEquals("" , activityInstance_Instance_3_0.getState(),ACTIVITY_STATE.RUNNING);
		Assert.assertEquals("" , activityInstance_Instance_4_0.getState(),ACTIVITY_STATE.RUNNING);
		
		// 节点3 提交后 新建join活动，有 theStart（完成状态） fork（完成状态） receivePayment(完成状态) shipOrder（运行状态） join（未开始状态）
		
		runtimeService.completeActivityInstance(activityInstance_Instance_3_0.getId());
		  
		Assert.assertEquals("" , runtimeService.getActivityInstanceByProcessInstId(processInstance.getId()).size(),5);
		ActivityInstance activityInstance_Instance_1_1 = runtimeService.getActivityInstanceByActivityDefId(processInstanceId, "theStart").iterator().next();
		ActivityInstance activityInstance_Instance_2_1 = runtimeService.getActivityInstanceByActivityDefId(processInstanceId, "fork").iterator().next();
		ActivityInstance activityInstance_Instance_3_1 = runtimeService.getActivityInstanceByActivityDefId(processInstanceId, "receivePayment").iterator().next();
		ActivityInstance activityInstance_Instance_4_1 = runtimeService.getActivityInstanceByActivityDefId(processInstanceId, "shipOrder").iterator().next();
		ActivityInstance activityInstance_Instance_5_1 = runtimeService.getActivityInstanceByActivityDefId(processInstanceId, "join").iterator().next();
		Assert.assertEquals("" , activityInstance_Instance_1_1.getState(),ACTIVITY_STATE.COMPLETED);
		Assert.assertEquals("" , activityInstance_Instance_2_1.getState(),ACTIVITY_STATE.COMPLETED);
		Assert.assertEquals("" , activityInstance_Instance_3_1.getState(),ACTIVITY_STATE.COMPLETED);
		Assert.assertEquals("" , activityInstance_Instance_4_1.getState(),ACTIVITY_STATE.RUNNING);
		Assert.assertEquals("" , activityInstance_Instance_5_1.getState(),ACTIVITY_STATE.NOT_STARTED);
		
		// 节点4 提交后 有 theStart（完成状态） fork（完成状态） receivePayment(完成状态) shipOrder（完成状态） join（完成状态）archiveOrder（运行状态）
		
		
		runtimeService.completeActivityInstance(activityInstance_Instance_4_1.getId());
		
		
		ActivityInstance activityInstance_Instance_1_2 = runtimeService.getActivityInstanceByActivityDefId(processInstanceId, "theStart").iterator().next();
		ActivityInstance activityInstance_Instance_2_2 = runtimeService.getActivityInstanceByActivityDefId(processInstanceId, "fork").iterator().next();
		ActivityInstance activityInstance_Instance_3_2 = runtimeService.getActivityInstanceByActivityDefId(processInstanceId, "receivePayment").iterator().next();
		ActivityInstance activityInstance_Instance_4_2 = runtimeService.getActivityInstanceByActivityDefId(processInstanceId, "shipOrder").iterator().next();
		ActivityInstance activityInstance_Instance_5_2 = runtimeService.getActivityInstanceByActivityDefId(processInstanceId, "join").iterator().next();
		ActivityInstance activityInstance_Instance_6_2 = runtimeService.getActivityInstanceByActivityDefId(processInstanceId, "archiveOrder").iterator().next();
		
		Assert.assertEquals("" , runtimeService.getActivityInstanceByProcessInstId(processInstance.getId()).size(),6);
		Assert.assertEquals("" , activityInstance_Instance_1_2.getState(),ACTIVITY_STATE.COMPLETED);
		Assert.assertEquals("" , activityInstance_Instance_2_2.getState(),ACTIVITY_STATE.COMPLETED);
		Assert.assertEquals("" , activityInstance_Instance_3_2.getState(),ACTIVITY_STATE.COMPLETED);
		Assert.assertEquals("" , activityInstance_Instance_4_2.getState(),ACTIVITY_STATE.COMPLETED);
		Assert.assertEquals("" , activityInstance_Instance_5_2.getState(),ACTIVITY_STATE.COMPLETED);
		Assert.assertEquals("" , activityInstance_Instance_6_2.getState(),ACTIVITY_STATE.RUNNING);
	
		// 节点6 提交后 将新建节点endState，endState是结束节点，将自动完成。  
		//theStart（完成状态） fork（完成状态） receivePayment(完成状态) shipOrder（完成状态） join（完成状态）archiveOrder（完成状态） theEnd（完成状态）
		// 流程是结束状态
		try{
			runtimeService.completeActivityInstance(activityInstance_Instance_6_2.getId());
		}catch(RunBPMException e){
			e.printStackTrace();
		}
		
		
		//已经转移到历史库中
//		ActivityInstance activityInstance_Instance_1_3 = runtimeService.getActivityInstanceByActivityDefId(processInstanceId, "theStart").iterator().next();
//		ActivityInstance activityInstance_Instance_2_3 = runtimeService.getActivityInstanceByActivityDefId(processInstanceId, "fork").iterator().next();
//		ActivityInstance activityInstance_Instance_3_3 = runtimeService.getActivityInstanceByActivityDefId(processInstanceId, "receivePayment").iterator().next();
//		ActivityInstance activityInstance_Instance_4_3 = runtimeService.getActivityInstanceByActivityDefId(processInstanceId, "shipOrder").iterator().next();
//		ActivityInstance activityInstance_Instance_5_3 = runtimeService.getActivityInstanceByActivityDefId(processInstanceId, "join").iterator().next();
//		ActivityInstance activityInstance_Instance_6_3 = runtimeService.getActivityInstanceByActivityDefId(processInstanceId, "archiveOrder").iterator().next();
//		ActivityInstance activityInstance_Instance_7_3 = runtimeService.getActivityInstanceByActivityDefId(processInstanceId, "theEnd").iterator().next();
//		  
//		Assert.assertEquals("" , runtimeService.getActivityInstanceByProcessInstId(processInstance.getId()).size(),7);
//		Assert.assertEquals("" , activityInstance_Instance_1_3.getState(),ACTIVITY_STATE.COMPLETED);
//		Assert.assertEquals("" , activityInstance_Instance_2_3.getState(),ACTIVITY_STATE.COMPLETED);
//		Assert.assertEquals("" , activityInstance_Instance_3_3.getState(),ACTIVITY_STATE.COMPLETED);
//		Assert.assertEquals("" , activityInstance_Instance_4_3.getState(),ACTIVITY_STATE.COMPLETED);
//		Assert.assertEquals("" , activityInstance_Instance_5_3.getState(),ACTIVITY_STATE.COMPLETED);
//		Assert.assertEquals("" , activityInstance_Instance_6_3.getState(),ACTIVITY_STATE.COMPLETED);
//		Assert.assertEquals("" , activityInstance_Instance_7_3.getState(),ACTIVITY_STATE.COMPLETED);
//		
//		newProcessInstance = runtimeService.getProcessInstance(processInstanceId);
//		Assert.assertEquals("" , newProcessInstance.getState(),PROCESS_STATE.COMPLETED);
		
	}
}
