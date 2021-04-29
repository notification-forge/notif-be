-- Dumping structure for table message-service.api_clients
CREATE TABLE IF NOT EXISTS `api_clients` (
  `api_client_id` varchar(55) NOT NULL,
  `created_by` varchar(255) NOT NULL,
  `created_timestamp` datetime NOT NULL,
  `last_modified_by` varchar(255) NOT NULL,
  `last_modified_timestamp` datetime NOT NULL,
  `access_key` varchar(512) NOT NULL,
  `name` varchar(55) NOT NULL,
  PRIMARY KEY (`api_client_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf16;

-- Data exporting was unselected.

-- Dumping structure for table message-service.hibernate_sequence
CREATE TABLE IF NOT EXISTS `hibernate_sequence` (
  `next_val` bigint(20) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf16;

-- Data exporting was unselected.

-- Dumping structure for table message-service.images
CREATE TABLE IF NOT EXISTS `images` (
  `image_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `created_by` varchar(255) NOT NULL,
  `created_timestamp` datetime NOT NULL,
  `last_modified_by` varchar(255) DEFAULT NULL,
  `last_modified_timestamp` datetime DEFAULT NULL,
  `app_code` varchar(24) NOT NULL,
  `content_type` varchar(255) NOT NULL,
  `file_name` varchar(255) NOT NULL,
  `file_signature` varchar(512) NOT NULL,
  `image_data` longblob NOT NULL,
  `status` varchar(255) NOT NULL,
  PRIMARY KEY (`image_id`),
  KEY `IDX_APPCODE_FILENAME` (`app_code`,`file_name`),
  CONSTRAINT `FKlwpgp2l70f7jj6e9r6nb7x6te` FOREIGN KEY (`app_code`) REFERENCES `tenants` (`app_code`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf16;

-- Data exporting was unselected.

-- Dumping structure for table message-service.messages
CREATE TABLE IF NOT EXISTS `messages` (
  `message_id` bigint(20) NOT NULL,
  `app_code` varchar(255) DEFAULT NULL,
  `message_status` varchar(255) DEFAULT NULL,
  `message_type` varchar(255) DEFAULT NULL,
  `reason` varchar(2000) DEFAULT NULL,
  `message_settings` varchar(255) DEFAULT NULL,
  `template_id` bigint(20) DEFAULT NULL,
  `template_version_id` bigint(20) DEFAULT NULL,
  `times_triggered` int(11) DEFAULT NULL,
  `content` varchar(255) DEFAULT NULL,
  `created_by` varchar(255) NOT NULL,
  `created_timestamp` datetime NOT NULL,
  `last_modified_by` varchar(255) DEFAULT NULL,
  `last_modified_timestamp` datetime DEFAULT NULL,
  PRIMARY KEY (`message_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf16;

-- Data exporting was unselected.

-- Dumping structure for table message-service.onboarding
CREATE TABLE IF NOT EXISTS `onboarding` (
  `onboarding_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `created_by` varchar(255) NOT NULL,
  `created_timestamp` datetime NOT NULL,
  `last_modified_by` varchar(255) DEFAULT NULL,
  `last_modified_timestamp` datetime DEFAULT NULL,
  `app_code` varchar(55) NOT NULL,
  `username` varchar(255) NOT NULL,
  PRIMARY KEY (`onboarding_id`),
  KEY `FK8wdess4wf2clvg597f0t9gfso` (`username`),
  KEY `FKdb033gw0d4imd3nkmw8bey805` (`app_code`),
  CONSTRAINT `FK8wdess4wf2clvg597f0t9gfso` FOREIGN KEY (`username`) REFERENCES `users` (`username`),
  CONSTRAINT `FKdb033gw0d4imd3nkmw8bey805` FOREIGN KEY (`app_code`) REFERENCES `tenants` (`app_code`)
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=utf16;

-- Data exporting was unselected.

-- Dumping structure for table message-service.plugins
CREATE TABLE IF NOT EXISTS `plugins` (
  `plugin_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `created_by` varchar(255) NOT NULL,
  `created_timestamp` datetime NOT NULL,
  `last_modified_by` varchar(255) DEFAULT NULL,
  `last_modified_timestamp` datetime DEFAULT NULL,
  `app_code` varchar(32) DEFAULT NULL,
  `configuration_class_name` varchar(255) DEFAULT NULL,
  `configuration_descriptor` text NOT NULL,
  `filename` varchar(255) DEFAULT NULL,
  `jar` mediumblob DEFAULT NULL,
  `name` varchar(255) NOT NULL,
  `plugin_class_name` varchar(255) DEFAULT NULL,
  `jar_url` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`plugin_id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf16;

-- Data exporting was unselected.

-- Dumping structure for table message-service.templates
CREATE TABLE IF NOT EXISTS `templates` (
  `template_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `created_by` varchar(255) NOT NULL,
  `created_timestamp` datetime NOT NULL,
  `last_modified_by` varchar(255) NOT NULL,
  `last_modified_timestamp` datetime NOT NULL,
  `notification_type` varchar(24) NOT NULL,
  `app_code` varchar(255) DEFAULT NULL,
  `template_name` varchar(255) DEFAULT NULL,
  `template_uuid` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`template_id`)
) ENGINE=InnoDB AUTO_INCREMENT=13 DEFAULT CHARSET=utf16;

-- Data exporting was unselected.

-- Dumping structure for table message-service.template_plugins
CREATE TABLE IF NOT EXISTS `template_plugins` (
  `template_plugin_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `created_by` varchar(255) NOT NULL,
  `created_timestamp` datetime NOT NULL,
  `last_modified_by` varchar(255) DEFAULT NULL,
  `last_modified_timestamp` datetime DEFAULT NULL,
  `configuration` text NOT NULL,
  `plugin_id` bigint(20) NOT NULL,
  `template_version_id` bigint(20) NOT NULL,
  PRIMARY KEY (`template_plugin_id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf16;

-- Data exporting was unselected.

-- Dumping structure for table message-service.template_versions
CREATE TABLE IF NOT EXISTS `template_versions` (
  `template_version_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `created_by` varchar(255) NOT NULL,
  `created_timestamp` datetime NOT NULL,
  `last_modified_by` varchar(255) NOT NULL,
  `last_modified_timestamp` datetime NOT NULL,
  `body` mediumtext NOT NULL,
  `template_version_name` varchar(255) DEFAULT NULL,
  `settings` mediumtext NOT NULL,
  `template_status` varchar(24) NOT NULL,
  `template_hash` int(11) NOT NULL,
  `template_id` bigint(20) DEFAULT NULL,
  `version` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`template_version_id`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf16;

-- Data exporting was unselected.

-- Dumping structure for table message-service.tenants
CREATE TABLE IF NOT EXISTS `tenants` (
  `app_code` varchar(24) NOT NULL,
  `created_by` varchar(255) NOT NULL,
  `created_timestamp` datetime NOT NULL,
  `last_modified_by` varchar(255) DEFAULT NULL,
  `last_modified_timestamp` datetime DEFAULT NULL,
  `api_token` varchar(255) DEFAULT NULL,
  `app_settings` text DEFAULT NULL,
  `approved_by` varchar(255) DEFAULT NULL,
  `approved_timestamp` datetime DEFAULT NULL,
  `description` varchar(255) DEFAULT NULL,
  `display_name` varchar(56) NOT NULL,
  `encryption_key` varchar(512) NOT NULL,
  `justification` varchar(255) DEFAULT NULL,
  `primary_owner_id` varchar(24) NOT NULL,
  `primary_owner_name` varchar(256) NOT NULL,
  `rejected_by` varchar(255) DEFAULT NULL,
  `rejected_timestamp` datetime DEFAULT NULL,
  `rejected_reason` varchar(255) DEFAULT NULL,
  `secondary_owner_id` varchar(24) NOT NULL,
  `secondary_owner_name` varchar(256) NOT NULL,
  `status` varchar(24) NOT NULL,
  PRIMARY KEY (`app_code`)
) ENGINE=InnoDB DEFAULT CHARSET=utf16;

-- Data exporting was unselected.

-- Dumping structure for table message-service.users
CREATE TABLE IF NOT EXISTS `users` (
  `username` varchar(255) NOT NULL,
  `name` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`username`)
) ENGINE=InnoDB DEFAULT CHARSET=utf16;