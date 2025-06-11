package dev.wiji.features.poll.controllers;

import dev.wiji.features.poll.models.Poll;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class PollIdManager {

    private static PollIdManager instance;
    private final Map<Integer, UUID> idToPollMap = new ConcurrentHashMap<>();
    private final Map<UUID, Integer> pollToIdMap = new ConcurrentHashMap<>();
    private final List<Poll> sortedPolls = new ArrayList<>();
    
    private int nextId = 1;
    
    private PollIdManager() {}
    
    public static PollIdManager getInstance() {
        if (instance == null) {
            instance = new PollIdManager();
        }
        return instance;
    }

    public synchronized void registerPoll(Poll poll) {
        if (pollToIdMap.containsKey(poll.getUuid())) {
            pollToIdMap.get(poll.getUuid());
            return;
        }
        
        int insertIndex = findInsertionIndex(poll);
        sortedPolls.add(insertIndex, poll);
        
        reassignIds();

        pollToIdMap.get(poll.getUuid());
    }

    public Poll getPollById(int id) {
        UUID pollUuid = idToPollMap.get(id);
        if (pollUuid == null) return null;
        
        for (Poll poll : sortedPolls) {
            if (poll.getUuid().equals(pollUuid)) return poll;
        }
        
        return null;
    }

    public Integer getPollId(Poll poll) {
        return pollToIdMap.get(poll.getUuid());
    }

    public Integer getPollId(UUID pollUuid) {
        return pollToIdMap.get(pollUuid);
    }

    public synchronized void removePoll(Poll poll) {
        removePoll(poll.getUuid());
    }

    public synchronized void removePoll(UUID pollUuid) {
        Integer id = pollToIdMap.get(pollUuid);
        if (id == null) return ;
        
        pollToIdMap.remove(pollUuid);
        idToPollMap.remove(id);
        
        sortedPolls.removeIf(poll -> poll.getUuid().equals(pollUuid));
        
        reassignIds();
    }

    public List<Poll> getAllPollsSorted() {
        return new ArrayList<>(sortedPolls);
    }

    public int getTotalPolls() {
        return sortedPolls.size();
    }

    public synchronized void clear() {
        idToPollMap.clear();
        pollToIdMap.clear();
        sortedPolls.clear();
        nextId = 1;
    }

    public synchronized void registerPolls(Collection<Poll> polls) {
        List<Poll> pollList = new ArrayList<>(polls);
        
        pollList.sort(Comparator.comparing(Poll::getCreationTimestamp));
        
        for (Poll poll : pollList) {
            if (!pollToIdMap.containsKey(poll.getUuid())) sortedPolls.add(poll);
        }
        
        reassignIds();
    }

    private int findInsertionIndex(Poll newPoll) {
        long newTimestamp = newPoll.getCreationTimestamp();
        
        for (int i = 0; i < sortedPolls.size(); i++) {
            if (sortedPolls.get(i).getCreationTimestamp() > newTimestamp) return i;
        }
        
        return sortedPolls.size();
    }

    private void reassignIds() {
        idToPollMap.clear();
        pollToIdMap.clear();
        
        for (int i = 0; i < sortedPolls.size(); i++) {
            int id = i + 1;
            Poll poll = sortedPolls.get(i);
            
            idToPollMap.put(id, poll.getUuid());
            pollToIdMap.put(poll.getUuid(), id);
        }
        
        nextId = sortedPolls.size() + 1;
    }
}