%{--
  - Copyright (c) 2011 - 2013 United ID.
  -
  - Licensed under the Apache License, Version 2.0 (the "License");
  - you may not use this file except in compliance with the License.
  - You may obtain a copy of the License at
  -
  - http://www.apache.org/licenses/LICENSE-2.0
  -
  - Unless required by applicable law or agreed to in writing, software
  - distributed under the License is distributed on an "AS IS" BASIS,
  - WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
  - See the License for the specific language governing permissions and
  - limitations under the License.
  --}%

<g:javascript library="jquery.uniform.min" />

<script type='text/javascript'>
  $(function(){
    $("select, input[type=submit]").uniform();
  });

  function handle_enter(event) {
    var evt = event || window.event;
    var code = evt.which || evt.keyCode || evt.charCode;
    if (13 == code) {
      if(evt.which) {
        evt.preventDefault();
      } else if (evt.keyCode) {
        evt.keyCode = 9;
			} else {
        evt.charCode = 9;
      }
		}
  }
</script>
