/*
 * Copyright 2018 the organization loushi135
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.lsq.springboot.datasource.starter.aspect.matcher;

public class RegexMatcher implements Matcher {

    private String pattern;

    private String dsGroup;

    public RegexMatcher() {
    }

    public RegexMatcher(String pattern, String dsGroup) {
        this.pattern = pattern;
        this.dsGroup = dsGroup;
    }

    public String getPattern() {
        return pattern;
    }

    public void setPattern(String pattern) {
        this.pattern = pattern;
    }

    public String getDsGroup() {
        return dsGroup;
    }

    public void setDsGroup(String dsGroup) {
        this.dsGroup = dsGroup;
    }
}
