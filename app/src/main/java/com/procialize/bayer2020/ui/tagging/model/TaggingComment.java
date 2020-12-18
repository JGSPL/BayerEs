package com.procialize.bayer2020.ui.tagging.model;

import com.percolate.mentions.Mentionable;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.util.List;

/**
 * Comment object.
 */
public class TaggingComment {

    private String comment;

    private List<Mentionable> mentions;

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public List<Mentionable> getMentions() {
        return mentions;
    }

    public void setMentions(List<Mentionable> mentions) {
        this.mentions = mentions;
    }
}
