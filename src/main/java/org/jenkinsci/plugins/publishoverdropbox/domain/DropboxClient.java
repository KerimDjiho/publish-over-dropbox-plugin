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

package org.jenkinsci.plugins.publishoverdropbox.domain;

import hudson.FilePath;
import jenkins.plugins.publish_over.BPBuildInfo;
import jenkins.plugins.publish_over.BPDefaultClient;
import jenkins.plugins.publish_over.BapPublisherException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jenkinsci.plugins.publishoverdropbox.domain.model.FolderMetadata;
import org.jenkinsci.plugins.publishoverdropbox.domain.model.RestException;
import org.jenkinsci.plugins.publishoverdropbox.impl.DropboxTransfer;
import org.jenkinsci.plugins.publishoverdropbox.impl.Messages;

import java.io.IOException;
import java.io.InputStream;

public class DropboxClient extends BPDefaultClient<DropboxTransfer> {

    private static final Log LOG = LogFactory.getLog(DropboxClient.class);
    private BPBuildInfo buildInfo;
    private final DropboxAdapter dropbox;
    private String token;

    public DropboxClient(final DropboxV2 client, final BPBuildInfo buildInfo) {
        this.dropbox = client;
        this.buildInfo = buildInfo;
    }

    public BPBuildInfo getBuildInfo() {
        return buildInfo;
    }

    public void setBuildInfo(final BPBuildInfo buildInfo) {
        this.buildInfo = buildInfo;
    }

    @Override
    public boolean changeDirectory(final String directory) {
        try {
            return dropbox.changeWorkingDirectory(directory);
        } catch (RestException e) {
            throw new BapPublisherException(Messages.exception_bap_changeDirectory(e.getMessage()), e);
        }
    }

    @Override
    public boolean makeDirectory(final String path) {
        try {
            FolderMetadata folder = dropbox.makeDirectory(path);
            return folder != null;
        } catch (RestException e) {
            throw new BapPublisherException(Messages.exception_bap_makeDirectory(e.getMessage()), e);
        }
    }

    @Override
    public void deleteTree() {
        try {
            dropbox.cleanWorkingFolder();
        } catch (RestException e) {
            throw new BapPublisherException(Messages.exception_bap_deleteTree(e.getMessage()), e);
        }
    }

    @Override
    public void beginTransfers(final DropboxTransfer transfer) {
        if (!transfer.hasConfiguredSourceFiles()) {
            throw new BapPublisherException(Messages.exception_noSourceFiles());
        }
        if (transfer.isRemoteDirectorySDF() && transfer.isPruneRoot()) {
            try {
                dropbox.pruneFolder(getAbsoluteRemoteRoot(), transfer.getPruneRootDays());
            } catch (RestException e) {
                throw new BapPublisherException(Messages.exception_bap_pruneFolder(e.getMessage()), e);
            }
        }
    }

    @Override
    public void transferFile(final DropboxTransfer transfer, final FilePath filePath, final InputStream content) {
        try {
            dropbox.storeFile(filePath.getName(), content, filePath.length());
        } catch (RestException e) {
            throw new BapPublisherException(Messages.exception_bap_transferFile(e.getMessage()), e);
        } catch (IOException e) {
            throw new BapPublisherException(Messages.exception_bap_transferFile(e.getMessage()), e);
        } catch (InterruptedException e) {
            throw new BapPublisherException(Messages.exception_bap_transferFile(e.getMessage()), e);
        }
    }

    public boolean connect() {
        try {
            return dropbox.isConnected() || dropbox.connect();
        } catch (RestException e) {
            throw new BapPublisherException(Messages.exception_bap_connect(e.getMessage()), e);
        }
    }

    @Override
    public void disconnect() {
        if ((dropbox != null) && dropbox.isConnected()) {
            dropbox.disconnect();
        }
    }

    @Override
    public void disconnectQuietly() {
        disconnect();
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getToken() {
        return token;
    }

    public void setTimeout(int timeout) {
        dropbox.setTimeout(timeout);
    }

    public int getTimeout() {
        return dropbox.getTimeout();
    }
}
