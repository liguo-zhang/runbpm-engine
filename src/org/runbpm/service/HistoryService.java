package org.runbpm.service;

import java.util.List;

import org.runbpm.entity.ActivityHistory;
import org.runbpm.entity.ProcessHistory;
import org.runbpm.entity.ProcessModelHistory;

public interface HistoryService {
	
	ProcessModelHistory loadLatestProcessDefinition(String processDefinitionId);
	
	ProcessHistory loadProcessHistory(long processHistoryId);

	List<ActivityHistory>  loadActivityHistoryByProcessInstId(long id);

	List<ActivityHistory> loadActivityHistoryByActivityDefId(Long processHistoryId, String activityDefinitionId);
	
}
