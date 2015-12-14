<#import "/_layouts/default.ftl" as layout>
<#import "/_macros/helpers.ftl" as helpers>
<@layout.defaultLayout "Account Registration">

<@helpers.showAllErrors "accountsEntryForm"/>

<form action="entry" method="POST">
<div style="width: 400px;">
  <div class="form-group">
    <label>E-mail</label>
    <@helpers.formInput "accountsEntryForm.email" "text"/>
  </div>
  <div class="form-group">
    <label>Password</label>
    <@helpers.formInput "accountsEntryForm.password" "password"/>
  </div>
  <div class="form-group">
    <label>Re-type Password</label>
    <@helpers.formInput "accountsEntryForm.retypedPassword" "password"/>
  </div>
</div>
<div>
  <input type="submit" value="Sign Up" class="btn btn-success">
</div>
</form>
</@layout.defaultLayout>
