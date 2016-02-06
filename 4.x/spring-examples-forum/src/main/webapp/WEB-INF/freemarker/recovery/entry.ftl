<#import "/_layouts/default.ftl" as layout>
<#import "/_macros/helpers.ftl" as helpers>
<@layout.defaultLayout "Account Recovery">
<@helpers.showAllErrors "recoveryEntryForm"/>
<form action="entry" method="POST">
<div style="width: 400px;">
  <div class="form-group">
    <label>E-mail</label>
    <@helpers.formInput "recoveryEntryForm.email" "text"/>
  </div>
</div>
<div>
  <input type="submit" value="Request Recovery" class="btn btn-primary">
</div>
</form>
</@layout.defaultLayout>
