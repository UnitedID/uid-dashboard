
You have been invited by %{--
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

${fieldValue(bean: inviteInstance, field: "fromName")} to try United ID's identity service.

This invite is valid until ${formatDate(date: (inviteInstance.dateCreated + 14), format: 'MMMM dd, yyyy')}.

To accept the invite click on the link below.

${inviteUrl}

To learn more about United ID please visit http://unitedid.org/

- United ID
