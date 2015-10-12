package org.assistments.service.controller;

import java.util.List;
import java.util.Map;

import org.assistments.connector.exception.ReferenceNotFoundException;
import org.assistments.service.domain.FolderItem;
import org.assistments.service.domain.Problem;
import org.assistments.service.domain.ProblemSection;
import org.assistments.service.domain.ProblemSet;

import com.google.gson.JsonArray;

/**
 * This interface represents Problem Set Management
 * @author szhao
 *
 */
public interface ProblemSetController  {
	
	/**
	 * Find a problem set information
	 * @param id -- identifier for the problem set
	 */
	public ProblemSet find(long id);
	
	/**
	 * Find a problem section by problem section id
	 * @param id -- problem section id
	 * @return problem section object
	 */
	public ProblemSection findBySectionId(long id);
	
	/**
	 * Find all problems inside a problem set
	 * @param problemSetId -- problem set id
	 * @return a list of problems
	 */
	public List<Problem> findAllProblems(long problemSetId);
	
	/**
	 * Check if a problem set is a skill builder or not
	 * @param problemSetId -- problem set id
	 * @return true -- if it is a skill builder
	 */
	public boolean isSkillBuilder(long problemSetId);
	
	/**
	 * Get problem set id of pseudo skill builder
	 * @param problemSetId -- problem set id
	 * @return problem set id
	 */
	public long getPseudoSkillBuilderId(long problemSetId);
	/**
	 * Check if a problem set is a pseudo skill builder
	 * @param problemSetId -- problem set id
	 * @return true -- if it is a pseudo skill builder
	 */
	public boolean isPseudoSkillBuilder(long problemSetId);
	
	public ProblemSet findByAssignment(String assignmentRef) throws ReferenceNotFoundException;
	
	/**
	 * get a list of problem sets by folder id
	 * @param id -- folder id
	 * @return a JsonArray represents problem sets under the folder
	 */
	
	public JsonArray getProblemSetsByFolder(long folderId);
	
	/**
	 * get a list of folder items by parent folder id
	 * @param id -- parent folder id
	 * @return a list of folder items
	 */
	
	public List<FolderItem> getFolderItemsByFolder(long folderId);
	/**
	 * get a list of folders
	 * @param folderIdList -- a list of folder ids
	 * @return a list of maps which contain folder name and folder id
	 */
	public List<Map<String, String>> getFoldersByIds(List<Integer> folderIdList);

	public List<Map<String, String>> getSubFoldersByFolderId(int folderId);

}
