<#import "/_layouts/default.ftl" as layout>
<#import "/_macros/helpers.ftl" as helpers>
<#if answersEditForm.answer?has_content>
  <#assign answer=answersEditForm.answer>
</#if>
<#assign question=answersEditForm.question>

<@layout.defaultLayout "Editing Answer">
<#if flash?has_content>
<div class="alert alert-success" data-role="flash">Your answer has been posted successfully.</div>
</#if>
<@helpers.showAllErrors "answersEditForm"/>
<form action="edit" method="POST">
<#if answer?has_content>
<input type="hidden" name="id" value="${answer.id}">
</#if>
<input type="hidden" name="questionId" value="${question.id}">
<div style="max-width: 640px">
  <#if answer?has_content>
  <div class="form-group">
    <div class="form-control-static"><strong>ID</strong>: ${answer.id}</div>
  </div>
  </#if>
  <div class="form-group">
    <label>Question</label>
    <div class="form-control-static"><a href="${config.url.basedir}/questions/${question.id}">${(question.subject)?html}</a></div>
  </div>
  <div class="form-group">
    <label>Body</label>
    <@helpers.formTextarea "answersEditForm.body"/>
  </div>
  <div class="form-group">
    <label>Status</label>
    <select name="status" class="form-control" style="width: auto">
      <@helpers.formOptions "answersEditForm.status" answersEditForm.statusMap/>
    </select>
  </div>
  <div>
    <input type="submit" value="Submit" class="btn btn-success">
    <a href="${config.url.basedir}/dashboard/answers" class="btn btn-default">Dashboard</a>
  </div>
</div>
</form>
</@layout.defaultLayout>
