/*
 * Copyright (c) 2012, Confluence Community
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms,
 * with or without modification, are permitted provided
 * that the following conditions are met:
 *
 *     + Redistributions of source code must retain the above
 *         copyright notice, this list of conditions and the following disclaimer.
 *     + Redistributions in binary form must reproduce the above copyright notice,
 *         this list of conditions and the following disclaimer in the documentation
 *         and/or other materials provided with the distribution.
 *     + Neither the name of Near Infinity Corporation nor the names of its contributors may
 *         be used to endorse or promote products derived from this software without
 *         specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO,
 * THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR
 * PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS
 * BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS;
 * OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY,
 * WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
 * OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package org.hivesoft.confluence.macros.vote.model;

import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * <p>
 * A model object representing a voting ballot. Ballots can have several
 * {@link Choice}s that can be voted on. Each <code>Choice</code> can be assigned
 * zero or more votes.
 * </p>
 */
public class Ballot {
    private String description=new String("");
    private String title;
    private Map<String, Choice> choices = new LinkedHashMap<String, Choice>();
    private Map<String, Comment> comments = new LinkedHashMap<String, Comment>();
    private boolean changeableVotes = false;
    private boolean visibleVoters=false; //1.1.7.5 show voters if allowed to
    private int startBound=1; //1.1.7.1 calculate for each ballot starting by 1
    private int iterateStep=1; //iterating Step, so usually it is 1 .. till choies.upperbound

    /**
     * <p>
     * Create a <code>Ballot</code>, specifying the title.
     * </p>
     *
     * @param title the title for the <code>Ballot</code>
     */
    public Ballot(String title) {
        this.title = title;
    }

    /**
     * <p>
     * Add an available {@link Choice} for this <code>Ballot</code>.
     * </p>
     *
     * @param choice a {@link Choice} for this <code>Ballot</code>
     */
    public void addChoice(Choice choice) {
        choices.put(choice.getDescription(), choice);
    }

    /**
     * <p>
     * Get the title assigned to the <code>Ballot</code>.
     * </p>
     *
     * @return title assigned to the <code>Ballot</code>
     */
    public String getTitle() {
        return title;
    }

   /**
	 * <p>
	 * Get the title assigned to the <code>Ballot</code> without spaces and lowercase (for linkage).
	 * </p>
	 *
	 * @return title assigned to the <code>Ballot</code> without spaces and lowercase (for linkage)
	 */
	public String getTitleNoSpace() {
		return title.replaceAll(" ", "").toLowerCase();
    }

    /**
     * <p>
     * Determine if a user has already voted.
     * </p>
     *
     * @param username the username of the prospective voter
     * @return <code>true</code> if the user has already voted on this
     * <code>Ballot</code>, <code>false</code> if s/he has not.
     */
    public boolean getHasVoted(String username) {
        return getVote(username) != null;
    }

    /**
     * <p>
     * Return whether or not users should be allowed to change
     * their vote once it has been cast. This ballot is not
     * responsible for enforcing this behaviour. It merely
     * tracks whether it should be allowed or not.
     * </p>
     *
     * @return <code>true</code> if users can change their vote; <code>false</code> (default) otherwise.
     */
    public boolean isChangeableVotes() {
        return changeableVotes;
    }

    /**
     * <p>
     * Set whether or not this ballot should allow users to change
     * their vote once it has been cast. This ballot is not
     * responsible for enforcing this behaviour. It merely
     * tracks whether it should be allowed or not.
     * </p>
     *
     * @param changeableVotes <code>true</code> if users can change their vote; <code>false</code> (default) otherwise.
     */
    public void setChangeableVotes(boolean changeableVotes) {
        this.changeableVotes = changeableVotes;
    }

    /**
	 * <p>
	 * Return whether or not users should be allowed to see voters in clear text
	 * </p>
	 *
	 * @return <code>true</code> if users can see voters; <code>false</code> (default) otherwise.
	 */
	public boolean isVisibleVoters() {
		return visibleVoters;
	}

	/**
	 * <p>
	 * Set whether or not this ballot should allow users to see the voters in clear text
	 * </p>
	 *
	 * @param visibleVotes <code>true</code> if users can see voters; <code>false</code> (default) otherwise.
	 */
	public void setVisibleVoters(boolean visibleVoters) {
		this.visibleVoters = visibleVoters;
    }

    /**
     * <p>
     * Get the choice voted for by a particular user
     * </p>
     *
     * @param username the username whose vote is needed
     * @return <code>Choice</code> that the user voted on, <code>null</code> if username has not voted.
     */
    public Choice getVote(String username) {
        Collection<Choice> col = choices.values();
        if (col != null && col.size() > 0) {
            for (Iterator<Choice> iter = col.iterator(); iter.hasNext(); ) {
                Choice choice = (Choice)iter.next();
                if (choice.getHasVotedFor(username)) {
                    return choice;
                }
            }
        }
        return null;
    }

    /**
     * <p>
     * Retrieve a {@link Choice} on the <code>Ballot</code> based on its description.
     * </p>
     *
     * @param description the description of the {@link Choice} to be retrieved
     * @return the {@link Choice} associated with the description
     */
    public Choice getChoice(String description) {
        return (Choice)choices.get(description);
    }

    /**
     * <p>
     * Get all of the {@link Choice}s belonging to this <code>Ballot</code>.
     * </p>
     *
     * @return all of the {@link Choice}s belonging to this <code>Ballot</code>
     */
    public Choice[] getChoices() {
        if (choices.size() > 0) {
            return (Choice[])choices.values().toArray(new Choice[choices.size()]);
        } else {
            return null;
        }
    }

    /**
     * <p>
     * Get a count of all votes that have been cast on the <code>Ballot</code>.
     * </p>
     *
     * @return a count of all votes that have been cast
     */
    public int getTotalVoteCount() {
        int totalVotes = 0;
        Collection<Choice> col = choices.values();
        if (col != null && col.size() > 0) {
            for (Iterator<Choice> iter = col.iterator(); iter.hasNext(); ) {
                Choice choice = (Choice)iter.next();
                totalVotes += choice.getVoteCount();
            }
        }
        return totalVotes;
    }

    /**
     * <p>
     * Get the percentage of the total vote represented by the provided
     * {@link Choice}.
     * </p>
     *
     * @param choice the {@link Choice} to determine the vote percentage of
     * @return the percentage of the total vote represented by the provided
     * {@link Choice}. The percentage is given as a whole number, rather than
     * a floating point number.
     */
    public int getPercentageOfVoteForChoice(Choice choice) {
        int totalVoteCount = getTotalVoteCount();
        if (totalVoteCount != 0) {
            return (100 * choice.getVoteCount()) / totalVoteCount;
        } else {
            return 0;
        }
    }

    /**
     * <p>
     * Set the description for this ballot.
     * </p>
     *
     * @return A String description of this ballot.
     */
    public String getDescription() {
        return description;
    }

    /**
     * <p>
     * Get the description for this ballot.
     * </p>
     *
     * @param description A String description of this ballot.
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * <p>
     * Add a user's comment to this ballot. Add comments need to have a username
     * associated with them before adding to a ballot.
     * </p>
     *
     * @param comment The comment to add.
     */
    public void addComment(Comment comment) {
        if (comment.getUsername() == null) {
            throw new IllegalArgumentException("All comments must have a username.");
        }

        comments.put(comment.getUsername(), comment);
    }

    /**
     * <p>
     * Remove a user's comment from this ballot.
     * </p>
     *
     * @param username The user that wishes to remove theis comment.
     */
    public void removeCommentForUser(String username) {
        comments.remove(username);
    }

    /**
     * <p>
     * Get the comment entered by a particular user.
     * </p>
     *
     * @param username The name of the user whose comment is to be returned.
     * @return The requested user's comment or null if not present.
     */
    public Comment getCommentForUser(String username) {
        return (Comment)comments.get(username);
    }

    /**
     * <p>
     * Get all of the comments entered on this ballot.
     * </p>
     *
     * @return All comments entered for this ballot.
     */
    public Map<String, Comment> getComments() {
        return comments;
    }

    /**
	 * <p>
	 * 1.1.7.1: set the StartBound for calculations (average)
	 * </p>
	 *
	 * @param Integer the Iteration shall start with
	 */
	public void setStartBound(int iStartBound) {
		startBound=iStartBound;
    }

    /**
	 * <p>
	 * 1.1.7.1: get the StartBound for calculations (average)
	 * </p>
	 *
	 * @return The StartBound for this ballots calcu/iteration.
	 */
	public int getStartBound() {
		return startBound;
    }

	/**
	 * <p>
	 * 1.1.7.1: set the iterateStep for calculations (average)
	 * </p>
	 *
	 * @param Integer the Iteration shall iterate with
	 */
	public void setIterateStep(int iIterateStep) {
		iterateStep=iIterateStep;
	}

	/**
	 * <p>
	 * 1.1.7.1: get the Iterations Step for calculations (average)
	 * </p>
	 *
	 * @return The iteration Step for this ballots calcu/iteration.
	 */
	public int getIterateStep() {
		return iterateStep;
    }

    /**
	 * @return The calculated EndBound Value (out of StartBound + iteration-steps)
	 */
	public int getEndBound() {
		return startBound+(choices.size()-1)*iterateStep;
    }

    /**
	 *
	 * @return The calculated <code>real<code> LowerBound Value
	 */
    public int getLowerBound() {
		return getStartBound()>getEndBound() ? getEndBound() : getStartBound();
	}

	/**
	 *
	 * @return The calculated <code>real<code> UpperBound Value
	 */
    public int getUpperBound() {
		return getStartBound()>getEndBound() ? getStartBound() : getEndBound();
	}

	public int getAveragePercentage(float average) {
		return (int)(average-getLowerBound())*100/(getUpperBound()-getLowerBound());
	}

	public String getBoundsIfNotDefault() {
		return (startBound==1 && iterateStep==1) ? "" : "("+getStartBound()+"-"+getEndBound()+")";
	}

    /**
     * <p>
     * Determines if a <code>Ballot</code> is equal to another <code>Ballot</code>.
     * Ballots are considered equal if their title is the same for both
     * ballots.
     * </p>
     *
     * @param o the <code>Object</code> to determine equality with this
     * <code>Ballot</code>
     * @return <code>true</code> if the ballot title of the <code>Object</code>
     * argument is the same as the title of this <code>Ballot</code>,
     * <code>false</code> otherwise.
     */
    public boolean equals(Object o) {
        if (o == null) {
            return false;
        }
        if (o instanceof Ballot) {
            return title.equals(((Ballot)o).title);
        }
        else {
            return false;
        }
    }

    /**
     * <p>
     * Return a hash code for the <code>Ballot</code>. The hash code for this
     * implementation is simply the hash code of the ballot title.
     * </p>
     *
     * @return hash code of the ballot title
     */
    public int hashCode() {
        return title.hashCode();
    }

    /** new in 1.1.7.5 by strike
	 * <p>
	 * Return a string of the stored voters, a comma separated list.
	 * </p>
	 *
	 * @return string of the ballot voters.
     */
    public String getAllVotersString(boolean bAsWikiMarkup) {
		//if (false) {
		try {
			LinkedHashMap<String, String> voters = new LinkedHashMap<String, String>();
			Collection<Choice> col;
			if(choices.size() > 0) {
				col = choices.values();
				for (Iterator<Choice> iter = col.iterator(); iter.hasNext(); ) {
					String[] choiceVoters = ((Choice)iter.next()).getVoters();
					if (choiceVoters != null)
						for(int iNo=0;iNo<choiceVoters.length;iNo++)
							voters.put(choiceVoters[iNo], choiceVoters[iNo]);
				}
			}
			StringBuilder bld = new StringBuilder();
			Collection<String> col2 = voters.values();
			if (col2 != null && col2.size() > 0) {
				for (Iterator<String> iter = col2.iterator(); iter.hasNext(); ) {
					String voter = (String)iter.next();
					if (bld.length() > 0 )
						bld.append(", ");
					if (bAsWikiMarkup)
						//bld.append("[~"+voter+"]");
					    bld.append("<ac:link><ri:user ri:username=\""+voter+"\" /></ac:link>");
					else
					bld.append(voter);
				}
			}
			return bld.toString();
		} catch(Exception e) {
			return e.getMessage();
		}
    }

}
