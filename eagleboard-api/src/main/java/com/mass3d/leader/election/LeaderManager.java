package com.mass3d.leader.election;

import com.mass3d.scheduling.SchedulingManager;

/**
 * Manages cluster leader node elections, renewals, revocations and to check whether
 * the current instance is the leader in the cluster.
 *
 */
public interface LeaderManager
{
    /**
     * Extend the expiration time of leadership if this node is the current leader.
     */
    void renewLeader();

    /**
     * Attempt to become the leader.
     */
    void electLeader();

    /**
     * Check if the current instance is the leader.
     *
     * @return true if this instance is the leader, false otherwise.
     */
    boolean isLeader();

    /**
     * Setter to set the scheduling manager to gain access to systems scheduling mechanisms.
     *
     * @param schedulingManager the instantiated scheduling manager.
     */
    void setSchedulingManager(SchedulingManager schedulingManager);

}