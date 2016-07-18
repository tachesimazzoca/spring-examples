<#import "/_layouts/default.ftl" as layout>
<#import "/_macros/helpers.ftl" as helpers>
<@layout.defaultLayout "Editing Question">
<#if flash?has_content && flash>
<div class="alert alert-success" data-role="flash">Your question has been posted successfully.</div>
</#if>
<@helpers.showAllErrors "questionEditForm"/>
<form action="edit" method="POST">
<#if questionEditForm.question?has_content>
<input type="hidden" name="id" value="${questionEditForm.question.id}">
</#if>
<div style="max-width: 640px">
  <#if question?has_content>
  <div class="form-group">
    <div class="form-control-static"><strong>ID</strong>: ${question.id}</div>
  </div>
  </#if>
  <div class="form-group">
    <label>Subject</label>
    <@helpers.formInput "questionEditForm.subject" "text"/>
  </div>
  <div class="form-group">
    <label>Body</label>
    <@helpers.formTextarea "questionEditForm.body"/>
  </div>
  <div class="form-group">
    <label>Status</label>
    <select name="status" class="form-control" style="width: auto">
      <@helpers.formOptions "questionEditForm.status" questionEditForm.statusMap/>
    </select>
  </div>
  <div>
    <input type="submit" value="Submit" class="btn btn-success">
    <#if questionEditForm.question?has_content>
    <a href="${config.url.basedir}/question/${questionEditForm.question.id}" class="btn btn-default">Browse</a>
    </#if>
    <a href="${config.url.basedir}/dashboard/question" class="btn btn-default">Dashboard</a>
  </div>
</div>
</form>
</@layout.defaultLayout>
