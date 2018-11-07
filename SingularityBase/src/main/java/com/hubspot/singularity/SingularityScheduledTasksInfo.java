package com.hubspot.singularity;

import java.util.ArrayList;
import java.util.List;

public class SingularityScheduledTasksInfo {

  private final int numFutureTasks;
  private final long maxTaskLag;
  private final long timestamp;
  private final List<SingularityPendingTaskId> lateTasks;

  private SingularityScheduledTasksInfo(List<SingularityPendingTaskId> lateTasks, int numFutureTasks, long maxTaskLag, long timestamp) {
    this.lateTasks = lateTasks;
    this.numFutureTasks = numFutureTasks;
    this.maxTaskLag = maxTaskLag;
    this.timestamp = timestamp;
  }

  public List<SingularityPendingTaskId> getLateTasks() {
    return lateTasks;
  }

  public int getNumLateTasks() { return getLateTasks().size(); }

  public int getNumFutureTasks() {
    return numFutureTasks;
  }

  public long getMaxTaskLag() {
    return maxTaskLag;
  }

  public long getTimestamp() {
    return timestamp;
  }

  public static SingularityScheduledTasksInfo getInfo(List<SingularityPendingTask> pendingTasks, long millisDeltaForLateTasks) {
    final long now = System.currentTimeMillis();

    int numFutureTasks = 0;
    long maxTaskLag = 0;
    List<SingularityPendingTaskId> lateTasks = new ArrayList<>();

    for (SingularityPendingTask pendingTask : pendingTasks) {
      long delta = now - pendingTask.getPendingTaskId().getNextRunAt();

      if (delta > millisDeltaForLateTasks) {
        lateTasks.add(pendingTask.getPendingTaskId());
      } else {
        numFutureTasks++;
      }

      if (delta > maxTaskLag) {
        maxTaskLag = delta;
      }
    }

    return new SingularityScheduledTasksInfo(lateTasks, numFutureTasks, maxTaskLag, now);
  }
}
