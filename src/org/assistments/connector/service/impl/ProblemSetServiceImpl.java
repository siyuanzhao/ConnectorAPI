package org.assistments.connector.service.impl;

import java.util.List;
import java.util.Map;

import org.assistments.connector.exception.ReferenceNotFoundException;
import org.assistments.connector.service.ProblemSetService;
import org.assistments.service.controller.ProblemSetController;
import org.assistments.service.controller.impl.ProblemSetControllerDAOImpl;
import org.assistments.service.domain.FolderItem;
import org.assistments.service.domain.Problem;
import org.assistments.service.domain.ProblemSection;
import org.assistments.service.domain.ProblemSet;

import com.google.gson.JsonArray;

public class ProblemSetServiceImpl implements ProblemSetService {
	
	ProblemSetController psc;
	
	public ProblemSetServiceImpl() {
		psc = new ProblemSetControllerDAOImpl();
	}

	@Override
	public ProblemSet find(long id) {
		ProblemSet ps = psc.find(id);

		return ps;
	}

	@Override
	public ProblemSection findBySectionId(long problemSectionId) {
		return psc.findBySectionId(problemSectionId);
	}

	@Override
	public ProblemSet findByAssignment(String assignmentRef) {
		try {
			return psc.findByAssignment(assignmentRef);
		} catch (ReferenceNotFoundException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public JsonArray getProblemSetsByFolder(long folderId) {
		JsonArray problemSets = psc.getProblemSetsByFolder(folderId);
		return problemSets;
	}

	@Override
	public List<Map<String, String>> getFoldersByIds(List<Integer> folderIdList) {
		return psc.getFoldersByIds(folderIdList);
	}

	@Override
	public List<FolderItem> getFolderItemsByFolder(long folderId) {
		return psc.getFolderItemsByFolder(folderId);
	}

	@Override
	public List<Problem> findAllProblems(long problemSetId) {
		return psc.findAllProblems(problemSetId);
	}

	@Override
	public boolean isSkillBuilder(long problemSetId) {
		return psc.isSkillBuilder(problemSetId);
	}

	@Override
	public List<Map<String, String>> getSubFoldersByFolderId(int folderId) {
		return psc.getSubFoldersByFolderId(folderId);
	}


	@Override
	public boolean isPseudoSkillBuilder(long problemSetId) {
		return psc.isPseudoSkillBuilder(problemSetId);
	}

	@Override
	public long getPseudoSkillBuilderId(long problemSetId) {
		return psc.getPseudoSkillBuilderId(problemSetId);
	}
	
}
