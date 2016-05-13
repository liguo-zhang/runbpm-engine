package org.runbpm;


import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.runbpm.bpmn.definition.ExclusiveGatewayTest;
import org.runbpm.bpmn.definition.ExtensionElementsTest;
import org.runbpm.bpmn.definition.ParallelGatewayTest;
import org.runbpm.bpmn.definition.SimpleUserTaskTest;
import org.runbpm.bpmn.definition.SubprocessTest;
import org.runbpm.bpmn.definition.Subprocess_nested;
import org.runbpm.bpmn.definition.build.SimpleBuildTest;
import org.runbpm.container.callactivity.*;
import org.runbpm.container.pattern.InclusiveGatewayContainerTest;
import org.runbpm.container.pattern.POJOBuildLineTest;
import org.runbpm.container.pattern.ParallelGatewayContainerTest;
import org.runbpm.container.pattern.exclusivegateway.ExclusiveGatewayContainerTest;
import org.runbpm.container.pattern.exclusivegateway.ExclusiveGatewayContainerTestClassName;
import org.runbpm.container.serviceTask.ServiceTaskContainerTest;
import org.runbpm.container.subprocess.SubprocessContainerTestMore;
import org.runbpm.container.subprocess.SubprocessNestedContainerTest;
import org.runbpm.container.subprocess.SubprocessContainerTest;
import org.runbpm.container.usertask.ExpressUserTaskContainerTest;
import org.runbpm.container.usertask.SimpleUserTaskContainerTest;
import org.runbpm.spring.execlusivegateway.ExclusiveGatewaySpringBeanTest;
import org.runbpm.spring.listener.activity.ActivityListenerTest_WithoutSpring;
import org.runbpm.spring.listener.activity.GlobalActivityListenerTest_WithSpring;
import org.runbpm.spring.listener.activity.GlobalActivityListenerTest_WithoutSpring;
import org.runbpm.spring.listener.process.GlobalProcessListener_WithSpring;
import org.runbpm.spring.listener.process.GlobalProcessListener_WithoutSpring;
import org.runbpm.spring.listener.process.ProcessListener_WithoutSpringTest;
import org.runbpm.spring.listener.usertask.CopyKeyTest_WithoutSpring;
import org.runbpm.spring.listener.usertask.GlobalUserTaskListener_WithSpring;
import org.runbpm.spring.listener.usertask.GlobalUserTaskListener_WithoutSpring;
import org.runbpm.spring.listener.usertask.UserTaskListener_WithoutSpring;
import org.runbpm.spring.persistence.hibernate.ParallelGateway_Hibernate;
import org.runbpm.spring.persistence.hibernate.ProcessComplete_Hibernate;
import org.runbpm.spring.persistence.redis.ParallelGateway_Redis;
import org.runbpm.spring.persistence.redis.ProcessComplete_Redis;
import org.runbpm.spring.persistence.redis_hibernate.ParallelGateway_RedisHibernate;
import org.runbpm.spring.persistence.redis_hibernate.ProcessComplete_RedisHibernate;
import org.runbpm.spring.usertask.UserTaskSprngBeanTest;


@RunWith(Suite.class)
@Suite.SuiteClasses({
	//org.runbpm.BPMN.definition
	ExclusiveGatewayTest.class,
	ExtensionElementsTest.class,
	ParallelGatewayTest.class,
	SimpleUserTaskTest.class,
	Subprocess_nested.class,
	SubprocessTest.class,

	//org.runbpm.BPMN.definition.build
	SimpleBuildTest.class,
	
	//org.runbpm.container.callactivity
	CallActivityTest.class,
	CallActivityInOutTest.class,
	
	//org.runbpm.container.exclusivegateway
	ExclusiveGatewayContainerTestClassName.class,
	ExclusiveGatewayContainerTest.class,	
	
	//org.runbpm.container.serviceTask
	ServiceTaskContainerTest.class,
	
	
	//org.runbpm.container.subprocess
	SubprocessContainerTestMore.class,
	SubprocessContainerTest.class,
	SubprocessNestedContainerTest.class,
	
	//org.runbpm.container.usertask
	SimpleUserTaskContainerTest.class,
	ExpressUserTaskContainerTest.class,
	
	//org.runbpm.container.pattern
	InclusiveGatewayContainerTest.class,
	ParallelGatewayContainerTest.class,
	POJOBuildLineTest.class,
	
	//org.runbpm.spring.execlusivegateway
	ExclusiveGatewaySpringBeanTest.class,
	
	//org.runbpm.spring.listener.activity
	GlobalActivityListenerTest_WithSpring.class,
	GlobalActivityListenerTest_WithoutSpring.class,
	ActivityListenerTest_WithoutSpring.class,
	
	//org.runbpm.spring.listener.process
	GlobalProcessListener_WithSpring.class,
	GlobalProcessListener_WithoutSpring.class,
	ProcessListener_WithoutSpringTest.class,
	
	//org.runbpm.spring.listener.usertask
	GlobalUserTaskListener_WithoutSpring.class,
	GlobalUserTaskListener_WithSpring.class,
	CopyKeyTest_WithoutSpring.class,
	UserTaskListener_WithoutSpring.class,
	
	//org.runbpm.spring.persistence.hibernate
	ParallelGateway_Hibernate.class,
	ProcessComplete_Hibernate.class,
	
	//org.runbpm.spring.persistence.redis
//	ParallelGateway_Redis.class,
//	ProcessComplete_Redis.class,
	
	//org.runbpm.spring.persistence.redis_hibernate
//	ParallelGateway_RedisHibernate.class,
//	ProcessComplete_RedisHibernate.class,
	
	//org.runbpm.spring.usertask
	UserTaskSprngBeanTest.class,
	
})

public class BPMUnitTestSuite {
  // the class remains empty,
  // used only as a holder for the above annotations
}