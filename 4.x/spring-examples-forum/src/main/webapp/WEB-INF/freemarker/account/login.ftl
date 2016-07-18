<#import "/_layouts/default.ftl" as layout>
<#import "/_macros/helpers.ftl" as helpers>
<@layout.defaultLayout "Sign In">
<@helpers.showAllErrors "accountLoginForm"/>
<div class="center-block" style="width: 400px">
<form action="login" method="POST">
<div class="well">
  <@helpers.formInput "accountLoginForm.returnTo" "hidden"/>
  <div class="form-group">
    <label>E-mail</label>
    <@helpers.formInput "accountLoginForm.email" "text"/>
  </div>
  <div class="form-group">
    <label>Password</label>
    <@helpers.formInput "accountLoginForm.password" "password"/>
  </div>
</div><!--/.well-->
<div>
  <input type="submit" value="Sign In" class="btn btn-primary">
  &nbsp;<a href="${config.url.basedir}/recovery/entry">Forgot Password</a>
</div>
</form>
<hr>
<p>If you don't have an account, <a href="${config.url.basedir}/account/entry">create a new account</a>.</p>
</div>
</@layout.defaultLayout>
