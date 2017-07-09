/*
 * The MIT License
 *
 * Copyright (C) 2017 by René de Groot
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

import org.junit.Before;
import org.junit.Test;

import java.io.UnsupportedEncodingException;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

public class FormBuilderTest {

    private FormBuilder sut;

    @Before
    public void setUp() throws Exception {
        sut = new FormBuilder();
    }

    @Test
    public void singleQuery() throws UnsupportedEncodingException {
        // Arrange
        sut.appendQueryParameter("key", "value");
        // Act
        String query = sut.build();
        // Assert
        assertThat(query, is("key=value"));
    }

    @Test
    public void threeQuery() throws UnsupportedEncodingException {
        // Arrange
        sut.appendQueryParameter("key", "value");
        sut.appendQueryParameter("key", "value");
        sut.appendQueryParameter("key", "value");
        // Act
        String query = sut.build();
        // Assert
        assertThat(query, is("key=value&key=value&key=value"));
    }

    @Test
    public void urlEncodingQuery() throws UnsupportedEncodingException {
        // Arrange
        sut.appendQueryParameter("k ey", "value");
        sut.appendQueryParameter("key", "val ue");
        sut.appendQueryParameter("ke y", "valu e");
        // Act
        String query = sut.build();
        // Assert
        assertThat(query, is("k+ey=value&key=val+ue&ke+y=valu+e"));
    }
}