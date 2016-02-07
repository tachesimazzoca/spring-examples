<#import "/_layouts/default.ftl" as layout>
<#import "/_macros/helpers.ftl" as helpers>
<@layout.defaultLayout "Editing Profile">
<#if flash?has_content && flash>
<div class="alert alert-success" data-role="flash">Your profile has been saved successfully.</div>
</#if>
<@helpers.showAllErrors "profileEditForm"/>
<form action="edit" method="POST">
<div style="max-width: 400px;">
  <div class="form-group">
    <label>E-mail</label>
    <@helpers.formInput "profileEditForm.email" "text"/>
  </div>
  <div class="form-group">
    <label>Current Password</label>
    <@helpers.formInput "profileEditForm.currentPassword" "password"/>
  </div>
  <div class="form-group">
    <label>Password</label>
    <@helpers.formInput "profileEditForm.password" "password"/>
  </div>
  <div class="form-group">
    <label>Re-type Password</label>
    <@helpers.formInput "profileEditForm.retypedPassword" "password"/>
  </div>
  <div class="form-group">
    <label>Nickname</label>
    <@helpers.formInput "profileEditForm.nickname" "text"/>
  </div>
</div>
<div>
  <input type="submit" value="Update" class="btn btn-success">
</div>
</form>
</@layout.defaultLayout>
