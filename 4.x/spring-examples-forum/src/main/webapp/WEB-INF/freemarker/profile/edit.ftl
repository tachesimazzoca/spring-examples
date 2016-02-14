<#import "/_layouts/default.ftl" as layout>
<#import "/_macros/helpers.ftl" as helpers>
<#assign account=user.account>
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

  <div class="form-group">
    <label>Icon</label>
    <div id="jsIconBlock">
      <#if icon>
      <div class="thumbnail">
        <img src="${config.url.basedir}/upload/profile/icon/${account.id}">
      </div>
      </#if>
    </div>
    <div id="jsTempIconBlock" style="display: none;">
      <div class="thumbnail clearfix">
        <div class="pull-right">
          <a href="#" onclick="return false;" id="jsRemoveTempIcon">
            <span class="glyphicon glyphicon-remove" style="color: #999"></span>
          </a>
        </div>
        <#if profileEditForm.iconToken?has_content>
        <img src="${config.url.basedir}/upload/temp/${profileEditForm.iconToken}" id="jsTempIconImg">
        <#else>
        <img src="" id="jsTempIconImg">
        </#if>
      </div>
    </div>
    <div id="jsIconError" class="alert alert-danger" style="display: none;">
      <p>Uploading failed. Please check the following conditions.</p>
      <ul>
        <li>The supported file formats are (jpg|png|gif).</li>
        <li>The size of the file must be equal or less than 1MB.</li>
      </ul>
    </div>
    <div>
      <input type="hidden" name="iconToken" id="jsIconTokenInput">
      <input type="file" name="file" id="jsIconFileInput">
    </div>
  </div>
</div>
<div>
  <input type="submit" value="Update" class="btn btn-success">
</div>
</form>

<script type="text/javascript">
(function($) {
  $(function() {
    var Uploader = {
      postTempFile: function(el) {
        var defer = $.Deferred();
        var fd = new FormData();
        fd.append('file', el.files[0]);
        $.ajax({
          url: '${config.url.basedir}/upload/temp'
        , data: fd
        , cache: false
        , contentType: false
        , processData: false
        , type: 'POST'
        , success: defer.resolve
        , error: defer.reject
        });
        return defer.promise();
      }
    };

    var showTempIcon = function(filename) {
      $('#jsIconBlock').hide();
      $('#jsIconTokenInput').attr('value', filename);
      $('#jsTempIconImg').attr('src', '${config.url.basedir}/upload/temp/' + filename);
      $('#jsTempIconBlock').show();
    };

    var hideTempIcon = function() {
      $('#jsTempIconBlock').hide();
      $('#jsIconTokenInput').attr('value', '');
      $('#jsIconBlock').show();
    };

    $('#jsIconFileInput').on("change", function() {
      Uploader.postTempFile(this).then(
        function(data) {
    	  $('#jsIconError').hide();
          showTempIcon(data);
        }
      , function(data) {
    	  hideTempIcon();
    	  $('#jsIconError').show();
        }
      );
    });

    $('#jsRemoveTempIcon').on("click", function() {
      hideTempIcon();
      var emptyIconFileInput = $('#jsIconFileInput').clone(true);
      $('#jsIconFileInput').replaceWith(emptyIconFileInput);
    });
  });
})(jQuery.noConflict());
</script>
</@layout.defaultLayout>
