INSERT INTO USERS (username, name) VALUES ('ntfusr1', 'Notification Forge User 1');
INSERT INTO USERS (username, name) VALUES ('ntfusr2', 'Notification Forge User 2');
INSERT INTO USERS (username, name) VALUES ('ntfapvr', 'Notification Forge Approver');

INSERT INTO TENANTS (app_code, display_name, primary_owner_name, primary_owner_id, secondary_owner_name, secondary_owner_id, api_token, status, description, encryption_key, justification, created_by, created_timestamp) VALUES ('FABK', 'Facebook', 'Notification Forge User 1', 'ntfusr1', 'Notification Forge User 3', 'ntfusr3', 'ABCDE12345', 'ACTIVE', 'Social Media App', '', 'Required to send email notification', 'SYSTEM', NOW());
INSERT INTO TENANTS (app_code, display_name, primary_owner_name, primary_owner_id, secondary_owner_name, secondary_owner_id, api_token, status, description, encryption_key, justification, created_by, created_timestamp) VALUES ('INST', 'Instagram', 'Notification Forge User 1', 'ntfusr1', 'Notification Forge User 3', 'ntfusr3', 'VWXYZ12345', 'ACTIVE', 'Social Media App', '', 'Required to send email notification', 'SYSTEM', NOW());
INSERT INTO TENANTS (app_code, display_name, primary_owner_name, primary_owner_id, secondary_owner_name, secondary_owner_id, api_token, status, description, encryption_key, justification, created_by, created_timestamp) VALUES ('TWIT', 'Twitter', 'Notification Forge User 2', 'ntfusr2', 'Notification Forge User 3', 'ntfusr3', 'VWXYZ67890', 'ACTIVE', 'Social Media App', '', 'Required to send email notification', 'SYSTEM', NOW());

INSERT INTO ONBOARDING (username, app_code, created_by, created_timestamp) VALUES ('ntfusr1', 'FABK', 'SYSTEM', NOW());
INSERT INTO ONBOARDING (username, app_code, created_by, created_timestamp) VALUES ('ntfusr1', 'INST', 'SYSTEM', NOW());
INSERT INTO ONBOARDING (username, app_code, created_by, created_timestamp) VALUES ('ntfusr2', 'TWIT', 'SYSTEM', NOW());
INSERT INTO ONBOARDING (username, app_code, created_by, created_timestamp) VALUES ('ntfapvr', 'FABK', 'SYSTEM', NOW());
INSERT INTO ONBOARDING (username, app_code, created_by, created_timestamp) VALUES ('ntfapvr', 'INST', 'SYSTEM', NOW());
INSERT INTO ONBOARDING (username, app_code, created_by, created_timestamp) VALUES ('ntfapvr', 'TWIT', 'SYSTEM', NOW());

INSERT INTO TEMPLATE_CFGS (name) VALUES ('EMAIL');
INSERT INTO TEMPLATE_CFGS (name) VALUES ('TEAMS');

INSERT INTO TEMPLATE_CFG_DETAILS (template_cfg_name, field_name, format, is_mandatory, created_by, created_timestamp) VALUES ('EMAIL', "sender", "", TRUE, "SYSTEM", NOW());
INSERT INTO TEMPLATE_CFG_DETAILS (template_cfg_name, field_name, format, is_mandatory, created_by, created_timestamp) VALUES ('EMAIL', "recipients", "", TRUE, "SYSTEM", NOW());
INSERT INTO TEMPLATE_CFG_DETAILS (template_cfg_name, field_name, format, is_mandatory, created_by, created_timestamp) VALUES ('EMAIL', "ccRecipients", "", TRUE, "SYSTEM", NOW());
INSERT INTO TEMPLATE_CFG_DETAILS (template_cfg_name, field_name, format, is_mandatory, created_by, created_timestamp) VALUES ('EMAIL', "bccRecipients", "", TRUE, "SYSTEM", NOW());
INSERT INTO TEMPLATE_CFG_DETAILS (template_cfg_name, field_name, format, is_mandatory, created_by, created_timestamp) VALUES ('EMAIL', "subject", "", TRUE, "SYSTEM", NOW());
INSERT INTO TEMPLATE_CFG_DETAILS (template_cfg_name, field_name, format, is_mandatory, created_by, created_timestamp) VALUES ('EMAIL', "importance", "", TRUE, "SYSTEM", NOW());
INSERT INTO TEMPLATE_CFG_DETAILS (template_cfg_name, field_name, format, is_mandatory, created_by, created_timestamp) VALUES ('EMAIL', "hasAttachments", "", TRUE, "SYSTEM", NOW());
INSERT INTO TEMPLATE_CFG_DETAILS (template_cfg_name, field_name, format, is_mandatory, created_by, created_timestamp) VALUES ('TEAMS', "teamsWebhookUrl", "", TRUE, "SYSTEM", NOW());