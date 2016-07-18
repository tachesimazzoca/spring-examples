<#import "/_layouts/default.ftl" as layout>
<#import "/_macros/helpers.ftl" as helpers>
<@layout.defaultLayout "Account Registration">
<@helpers.showAllErrors "accountEntryForm"/>
<form action="entry" method="POST">
<div style="width: 400px;">
  <div class="form-group">
    <label>E-mail</label>
    <@helpers.formInput "accountEntryForm.email" "text"/>
  </div>
  <div class="form-group">
    <label>Password</label>
    <@helpers.formInput "accountEntryForm.password" "password"/>
  </div>
  <div class="form-group">
    <label>Re-typed Password</label>
    <@helpers.formInput "accountEntryForm.retypedPassword" "password"/>
  </div>
</div>
<div>
  <input type="submit" value="Sign Up" class="btn btn-success">
</div>
</form>
</@layout.defaultLayout>
