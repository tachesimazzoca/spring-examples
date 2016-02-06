<#import "/_layouts/default.ftl" as layout>
<#import "/_macros/helpers.ftl" as helpers>
<@layout.defaultLayout "Account Recovery">
<@helpers.showAllErrors "recoveryResetForm"/>
<form action="reset" method="POST">
<@helpers.formHiddenInput "recoveryResetForm.code"/>
<div style="width: 400px;">
  <div class="form-group">
    <label>Password</label>
    <@helpers.formInput "recoveryResetForm.password" "password"/>
  </div>
  <div class="form-group">
    <label>Re-type Password</label>
    <@helpers.formInput "recoveryResetForm.retypedPassword" "password"/>
  </div>
</div>
<div>
  <input type="submit" value="Update Password" class="btn btn-success">
</div>
</form>
</@layout.defaultLayout>
