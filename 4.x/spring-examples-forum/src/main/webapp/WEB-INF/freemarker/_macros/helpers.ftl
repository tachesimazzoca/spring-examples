<#ftl strip_whitespace=true>

<#macro message code>${springMacroRequestContext.getMessage(code)}</#macro>

<#macro messageText code, text>${springMacroRequestContext.getMessage(code, text)}</#macro>

<#macro messageArgs code, args>${springMacroRequestContext.getMessage(code, args)}</#macro>

<#macro messageArgsText code, args, text>${springMacroRequestContext.getMessage(code, args, text)}</#macro>

<#macro url relativeUrl extra...><#if extra?? && extra?size!=0>${springMacroRequestContext.getContextUrl(relativeUrl,extra)}<#else>${springMacroRequestContext.getContextUrl(relativeUrl)}</#if></#macro>

<#macro bind path>
    <#if htmlEscape?exists>
        <#assign status = springMacroRequestContext.getBindStatus(path, htmlEscape)>
    <#else>
        <#assign status = springMacroRequestContext.getBindStatus(path)>
    </#if>
    <#-- assign a temporary value, forcing a string representation for any
    kind of variable. This temp value is only used in this macro lib -->
    <#if status.value?exists && status.value?is_boolean>
        <#assign stringStatusValue=status.value?string>
    <#else>
        <#assign stringStatusValue=status.value?default("")>
    </#if>
</#macro>

<#macro bindEscaped path, htmlEscape>
    <#assign status = springMacroRequestContext.getBindStatus(path, htmlEscape)>
    <#-- assign a temporary value, forcing a string representation for any
    kind of variable. This temp value is only used in this macro lib -->
    <#if status.value?exists && status.value?is_boolean>
        <#assign stringStatusValue=status.value?string>
    <#else>
        <#assign stringStatusValue=status.value?default("")>
    </#if>
</#macro>

<#macro formInput path attributes="" fieldType="text" fillIn=true>
    <@bind path/>
    <input type="${fieldType}" name="${status.expression}" value="<#if fillIn>${stringStatusValue}</#if>" ${attributes}<@closeTag/>
</#macro>

<#macro formTextarea path attributes="">
    <@bind path/>
    <textarea name="${status.expression}" ${attributes}>${stringStatusValue}</textarea>
</#macro>

<#macro formSingleSelect path options attributes="">
    <@bind path/>
    <select name="${status.expression}" ${attributes}>
        <#if options?is_hash>
            <#list options?keys as value>
            <option value="${value?html}"<@checkSelected value/>>${options[value]?html}</option>
            </#list>
        <#else>
            <#list options as value>
            <option value="${value?html}"<@checkSelected value/>>${value?html}</option>
            </#list>
        </#if>
    </select>
</#macro>

<#macro formMultiSelect path options attributes="">
    <@bind path/>
    <select multiple="multiple" name="${status.expression}" ${attributes}>
        <#list options?keys as value>
        <#assign isSelected = contains(status.actualValue?default([""]), value)>
        <option value="${value?html}"<#if isSelected> selected="selected"</#if>>${options[value]?html}</option>
        </#list>
    </select>
</#macro>

<#macro formRadioButtons path options separator attributes="">
    <@bind path/>
    <#list options?keys as value>
    <input type="radio" id="${id}" name="${status.expression}" value="${value?html}"<#if stringStatusValue == value> checked="checked"</#if> ${attributes}<@closeTag/>
    <label>${options[value]?html}</label>${separator}
    </#list>
</#macro>

<#macro formCheckboxes path options separator attributes="">
    <@bind path/>
    <#list options?keys as value>
    <#assign isSelected = contains(status.actualValue?default([""]), value)>
    <input type="checkbox" id="${id}" name="${status.expression}" value="${value?html}"<#if isSelected> checked="checked"</#if> ${attributes}<@closeTag/>
    <label>${options[value]?html}</label>${separator}
    </#list>
    <input type="hidden" name="_${status.expression}" value="on"/>
</#macro>

<#macro formCheckbox path attributes="">
	<@bind path />
    <#assign isSelected = status.value?? && status.value?string=="true">
	<input type="hidden" name="_${status.expression}" value="on"/>
	<input type="checkbox" name="${status.expression}"<#if isSelected> checked="checked"</#if> ${attributes}/>
</#macro>

<#macro showErrors separator classOrStyle="">
    <#list status.errorMessages as error>
    <#if classOrStyle == "">
        <b>${error}</b>
    <#else>
        <#if classOrStyle?index_of(":") == -1><#assign attr="class"><#else><#assign attr="style"></#if>
        <span ${attr}="${classOrStyle}">${error}</span>
    </#if>
    <#if error_has_next>${separator}</#if>
    </#list>
</#macro>

<#macro checkSelected value>
    <#if stringStatusValue?is_number && stringStatusValue == value?number>selected="selected"</#if>
    <#if stringStatusValue?is_string && stringStatusValue == value>selected="selected"</#if>
</#macro>

<#function contains list item>
    <#list list as nextInList>
    <#if nextInList == item><#return true></#if>
    </#list>
    <#return false>
</#function>

<#macro closeTag>
    <#if xhtmlCompliant?exists && xhtmlCompliant>/><#else>></#if>
</#macro>