<#import "/_layouts/default.ftl" as layout>
<@layout.defaultLayout "Account Recovery">
<p>The verification code has been expired. Sorry, try again.</p>
<div>
  <a href="${config.url.basedir}/recovery/entry" class="btn btn-default">Try again</a>
</div>
</@layout.defaultLayout>
