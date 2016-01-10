<#import "/_layouts/default.ftl" as layout>
<#import "/_macros/helpers.ftl" as helpers>
<@layout.defaultLayout "Editing Question">
<#if flash?has_content>
<div class="alert alert-success" data-role="flash">Your question has been posted successfully.</div>
</#if>
<@helpers.showAllErrors "questionsEditForm"/>
<form action="edit" method="POST">
<#if questionsEditForm.question?has_content>
<input type="hidden" name="id" value="${questionsEditForm.question.id}">
</#if>
<div style="max-width: 640px">
  <#if question?has_content>
  <div class="form-group">
    <div class="form-control-static"><strong>ID</strong>: ${question.id}</div> 
  </div>
  </#if>
  <div class="form-group">
    <label>Subject</label>
    <@helpers.formInput "questionsEditForm.subject" "text"/>
  </div>
  <div class="form-group">
    <label>Body</label>
    <@helpers.formTextarea "questionsEditForm.body"/>
  </div>
  <div class="form-group">
    <label>Status</label>
    <select name="status" class="form-control" style="width: auto">
      <@helpers.formOptions "questionsEditForm.status" questionsEditForm.statusMap/>
    </select>
  </div>
  <div>
    <input type="submit" value="Submit" class="btn btn-success">
    <#if questionsEditForm.question?has_content>
    <a href="${config.url.basedir}/questions/${questionsEditForm.question.id}" class="btn btn-default">Browse</a>
    </#if>
    <a href="${config.url.basedir}/dashboard/questions" class="btn btn-default">Dashboard</a>
  </div>
</div>
</form>
</@layout.defaultLayout>
