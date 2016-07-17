<#ftl strip_whitespace=true>

<#function message code>
    <#return springMacroRequestContext.getMessage(code, code)/>
</#function>

<#function messageResolvable resolvable>
    <#return springMacroRequestContext.getMessage(resolvable)/>
</#function>

<#function url relativeUrl extra...>
    <#if extra?? && extra?is_hash>
        <#return springMacroRequestContext.getContextUrl(relativeUrl, extra)/>
    <#else>
        <#return springMacroRequestContext.getContextUrl(relativeUrl)/>
    </#if>
</#function>

<#macro closeTag>
    <#if xhtmlCompliant?exists && xhtmlCompliant>/><#else>></#if>
</#macro>

<#macro formInput path fieldType="text" attributes="class=\"form-control\"">
    <#assign _status=springMacroRequestContext.getBindStatus(path, false)/>
    <#if _status.value?exists && _status.value?is_boolean>
        <#assign _statusValue=_status.value?string>
    <#else>
        <#assign _statusValue=_status.value?default("")>
    </#if>
    <input type="${fieldType}" name="${_status.expression?html}" value="${_statusValue?html}" ${attributes}<@closeTag/>
</#macro>

<#macro formPasswordInput path attributes="class=\"form-control\"">
    <#assign _status=springMacroRequestContext.getBindStatus(path, false)/>
    <#if _status.value?exists && _status.value?is_boolean>
        <#assign _statusValue=_status.value?string>
    <#else>
        <#assign _statusValue=_status.value?default("")>
    </#if>
    <input type="password" name="${_status.expression?html}" ${attributes}<@closeTag/>
</#macro>

<#macro formHiddenInput path>
    <#assign _status=springMacroRequestContext.getBindStatus(path, false)/>
    <#if _status.value?exists && _status.value?is_boolean>
        <#assign _statusValue=_status.value?string>
    <#else>
        <#assign _statusValue=_status.value?default("")>
    </#if>
    <input type="hidden" name="${_status.expression?html}" value="${_statusValue?html}"<@closeTag/>
</#macro>

<#macro formTextarea path attributes="class=\"form-control\"">
    <#assign _status=springMacroRequestContext.getBindStatus(path, false)/>
    <#if _status.value?exists && _status.value?is_boolean>
        <#assign _statusValue=_status.value?string>
    <#else>
        <#assign _statusValue=_status.value?default("")>
    </#if>
    <textarea name="${_status.expression?html}" ${attributes}>${_statusValue?html}</textarea>
</#macro>

<#macro formOptions path options attributes="">
    <#assign _status=springMacroRequestContext.getBindStatus(path, false)/>
    <#if _status.value?exists && _status.value?is_boolean>
        <#assign _statusValue=_status.value?string>
    <#else>
        <#assign _statusValue=_status.value?default("")>
    </#if>
    <#if options?is_hash>
        <#list options?keys as value>
        <option value="${value?html}"<#if _statusValue == value> selected="selected"</#if>>${options[value]?html}</option>
        </#list>
    <#else>
        <#list options as value>
        <option value="${value?html}"<#if _statusValue == value> selected="selected"</#if>>${value?html}</option>
        </#list>
    </#if>
</#macro>

<#macro formRadioButtons path options separator attributes="">
    <#assign _status=springMacroRequestContext.getBindStatus(path, false)/>
    <#if _status.value?exists && _status.value?is_boolean>
        <#assign _statusValue=_status.value?string>
    <#else>
        <#assign _statusValue=_status.value?default("")>
    </#if>
    <#list options?keys as value>
    <input type="radio" id="${id}" name="${status.expression?html}" value="${value?html}"<#if _statusValue == value> checked="checked"</#if> ${attributes}<@closeTag/>
    <label>${options[value]?html}</label>${separator}
    </#list>
</#macro>

<#macro formCheckboxes path options separator attributes="">
    <#assign _status=springMacroRequestContext.getBindStatus(path, false)/>
    <#list options?keys as value>
    <#assign isSelected=_status.actualValue?default([""])?seq_contains(value)/>
    <input type="checkbox" name="${_status.expression?html}" value="${value?html}"<#if isSelected> checked="checked"</#if> ${attributes}<@closeTag/>
    <label>${options[value]?html}</label>${separator}
    </#list>
    <input type="hidden" name="_${_status.expression?html}" value="on"/>
</#macro>

<#macro formCheckbox path attributes="">
    <#assign _status=springMacroRequestContext.getBindStatus(path, false)/>
    <#if _status.value?exists && _status.value?is_boolean>
        <#assign _statusValue=_status.value?string>
    <#else>
        <#assign _statusValue=_status.value?default("")>
    </#if>
    <#assign isSelected=(status.value?? && status.value?string == "true")>
    <input type="hidden" name="_${_status.expression?html}" value="on"/>
    <input type="checkbox" name="${_status.expression?html}"<#if isSelected> checked="checked"</#if> ${attributes}/>
</#macro>

<#macro showAllErrors path attributes="class=\"alert alert-danger\"">
    <#assign _status=springMacroRequestContext.getBindStatus(path, false)/>
    <#if _status.errors.hasErrors()>
    <div ${attributes}>
        <ul>
        <#list _status.errors.allErrors as error>
            <li>${messageResolvable(error)?html}</li>
        </#list>
        </ul>
    </div>
    </#if>
</#macro>

<#macro showGlobalErrors path attributes="class=\"alert alert-danger\"">
    <#assign _status=springMacroRequestContext.getBindStatus(path, false)/>
    <#if _status.errors.hasGlobalErrors()>
    <div ${attributes}>
        <ul>
        <#list _status.errors.globalErrors as error>
            <li>${messageResolvable(error)?html}</li>
        </#list>
        </ul>
    </div>
    </#if>
</#macro>

<#macro showFieldErrors path attributes="class=\"alert alert-danger\"">
    <#assign _status=springMacroRequestContext.getBindStatus(path, false)/>
    <#if (_status.errorMessages?size > 0)>
    <div ${attributes}>
        <ul>
        <#list _status.errorMessages as _msg>
            <li>${_msg?html}</li>
        </#list>
        </ul>
    </div>
    </#if>
</#macro>
