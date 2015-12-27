<#import "/_layouts/default.ftl" as layout>
<#import "/_macros/helpers.ftl" as helpers>
<#assign htmlEscape=true in helpers>

<@layout.defaultLayout "Sign In">

<@helpers.showAllErrors "accountsLoginForm"/>

<div class="center-block" style="width: 400px">
<form action="login" method="POST">
<div class="well">
  <@helpers.formInput "accountsLoginForm.returnTo" "hidden"/>
  <div class="form-group">
    <label>E-mail</label>
    <@helpers.formInput "accountsLoginForm.email" "text"/>
  </div>
  <div class="form-group">
    <label>Password</label>
    <@helpers.formInput "accountsLoginForm.password" "password"/>
  </div>
</div><!--/.well-->
<div>
  <input type="submit" value="Sign In" class="btn btn-primary">
  &nbsp;<a href="${config.url.basedir}/recovery/entry">Forgot Password</a>
</div>
</form>
<hr>
<p>If you don't have an account, <a href="${config.url.basedir}/accounts/entry">create a new account</a>.</p>
</div>
</@layout.defaultLayout>
