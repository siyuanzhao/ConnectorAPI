package org.assistments.connector.service;

import java.util.List;
import java.util.Map;

import org.assistments.service.controller.ProblemSetController;
import org.assistments.service.domain.FolderItem;
import org.assistments.service.domain.ProblemSection;
import org.assistments.service.domain.ProblemSet;

import com.google.gson.JsonArray;

/**
 * This interface contain the operations on the problem set
 * @author szhao
 *
 */
public interface ProblemSetService extends ProblemSetController {

	/**
	 * Find a list of problem set
	 * @param folderId -- folder id 
	 * @return a JsonArray represents list of problem set under the given folder
	 */
	@Override
	public JsonArray getProblemSetsByFolder(long folderId);
	
	/**
	 * Find a list of folder
	 * @param folderIdList -- a list of folder ids
	 * @return a List of maps which contains folder name and folder id
	 */
	@Override
	public List<Map<String, String>> getFoldersByIds(List<Integer> folderIdList);
	
	/**
	 * Find a list of problem set
	 * @param folderId -- folder id 
	 * @return a JsonArray represents list of problem set under the given folder
	 */
	@Override
	public List<FolderItem> getFolderItemsByFolder(long folderId);
	
	@Override
	public ProblemSection findBySectionId(long problemSectionId);
	
	@Override
	public ProblemSet findByAssignment(String assignmentRef);
	
	@Override
	public List<Map<String, String>> getSubFoldersByFolderId(int folderId);
	
}
