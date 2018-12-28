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

package com.lsq.springboot.datasource.starter.autoconfigure;

import com.google.common.collect.Lists;
import com.lsq.springboot.datasource.starter.aspect.matcher.ExpressionMatcher;
import com.lsq.springboot.datasource.starter.aspect.matcher.Matcher;
import com.lsq.springboot.datasource.starter.aspect.matcher.RegexMatcher;

import java.util.List;

public class MultiDataSourceMatcherConfig {

    private List<Matcher> matchers = Lists.newArrayList();

    public static MultiDataSourceMatcherConfig newInstance() {
        return new MultiDataSourceMatcherConfig();
    }

    public MultiDataSourceMatcherConfig regexMatcher(String pattern, String dsGroup) {
        matchers.add(new RegexMatcher(pattern, dsGroup));
        return this;
    }

    public MultiDataSourceMatcherConfig expressionMatcher(String pattern, String dsGroup) {
        matchers.add(new ExpressionMatcher(pattern, dsGroup));
        return this;
    }

    public List<Matcher> getMatchers() {
        return matchers;
    }

    public void setMatchers(List<Matcher> matchers) {
        this.matchers = matchers;
    }
}
