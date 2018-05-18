package com.anbang.qipai.members.cqrs.c.domain;

import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class MemberIdManager {

	// id id 存放的是相同的memberid。考虑到线程安全和查询效率。 不使用list，set 。而是使用map
	private Map<String, String> idMap = new ConcurrentHashMap<>();

	private static char[] charsForId = new char[] { '0', '1', '2', '3', '5', '6', '7', '8', '9' };

	public String createMemberId(long seed) {
		Random random = new Random(seed);
		String newId;
		while (true) {
			int charCount = 0;
			int numCount = 0;
			StringBuilder sb = new StringBuilder();
			for (int i = 0; i < 6; i++) {
				int charIdx = random.nextInt(charsForId.length);
				charCount++;
				sb.append(charsForId[charIdx]);
			}
			newId = sb.toString();
			if (idMap.containsKey(newId)) {
				continue;
			} else {
				break;
			}
		}

		idMap.put(newId, newId);
		return newId;
	}

	public Set<String> takeAllMemberId() {
		return idMap.keySet();
	}

	public boolean hasMember(String memberId) {
		return idMap.containsKey(memberId);
	}

	public void removeMemberId(String memberId) {
		idMap.remove(memberId);
	}

	public Map<String, String> getIdMap() {
		return idMap;
	}

	public void setIdMap(Map<String, String> idMap) {
		this.idMap = idMap;
	}

}
