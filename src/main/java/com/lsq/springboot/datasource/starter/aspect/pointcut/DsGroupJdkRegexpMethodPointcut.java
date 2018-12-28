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

package com.lsq.springboot.datasource.starter.aspect.pointcut;

import org.springframework.aop.support.JdkRegexpMethodPointcut;

import java.util.Map;

public class DsGroupJdkRegexpMethodPointcut extends JdkRegexpMethodPointcut {

    private Map<String, String> matchesCache;

    private String dsGroup;

    public DsGroupJdkRegexpMethodPointcut(String pattern, String dsGroup, Map<String, String> matchesCache) {
        this.dsGroup = dsGroup;
        this.matchesCache = matchesCache;
        setPattern(pattern);
    }

    @Override
    protected boolean matches(String pattern, int patternIndex) {
        boolean matches = super.matches(pattern, patternIndex);
        if (matches) {
            matchesCache.put(pattern, dsGroup);
        }
        return matches;
    }

}
