/*
 * The MIT License
 *
 * Copyright (C) 2015 by René de Groot
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

package org.jenkinsci.plugins.publishoverdropbox.impl;

import hudson.model.Describable;
import jenkins.model.Jenkins;
import jenkins.plugins.publish_over.BapPublisher;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.jenkinsci.plugins.publishoverdropbox.descriptor.DropboxPublisherDescriptor;
import org.kohsuke.stapler.DataBoundConstructor;

import java.util.ArrayList;

public class DropboxPublisher extends BapPublisher<DropboxTransfer> implements Describable<DropboxPublisher> {

    private static final long serialVersionUID = 1L;

    @DataBoundConstructor
    public DropboxPublisher(final String configName, final boolean verbose, final ArrayList<DropboxTransfer> transfers,
                            final boolean useWorkspaceInPromotion, final boolean usePromotionTimestamp, final DropboxRetry dropboxRetry,
                            final DropboxPublisherLabel dropboxLabel) {
        super(configName, verbose, transfers, useWorkspaceInPromotion, usePromotionTimestamp, dropboxRetry, dropboxLabel, null);
    }

    public DropboxRetry getDropboxRetry() {
        return (DropboxRetry) super.getRetry();
    }

    public DropboxPublisherLabel getDropboxLabel() {
        return (DropboxPublisherLabel) super.getLabel();
    }

    public DropboxPublisherDescriptor getDescriptor() {
        return Jenkins.getActiveInstance().getDescriptorByType(DropboxPublisherDescriptor.class);
    }

    public boolean equals(final Object that) {
        if (this == that) return true;
        if (that == null || getClass() != that.getClass()) return false;

        return addToEquals(new EqualsBuilder(), (DropboxPublisher) that).isEquals();
    }

    public int hashCode() {
        return addToHashCode(new HashCodeBuilder()).toHashCode();
    }

    public String toString() {
        return addToToString(new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)).toString();
    }
}
