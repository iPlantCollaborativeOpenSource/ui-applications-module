package org.iplantc.core.uiapplications.client.models.autobeans;

import com.google.web.bindery.autobean.shared.AutoBean.PropertyName;

public interface AnalysisFeedback {

    @PropertyName("average")
    double getAverageRating();

    @PropertyName("user")
    int getUserRating();

    @PropertyName("comment_id")
    long getCommentId();

    @PropertyName("average")
    void setAverageRating(double averageRating);

    @PropertyName("user")
    void setUserRating(int userRating);

    @PropertyName("comment_id")
    void setCommentId(long commentId);
}
