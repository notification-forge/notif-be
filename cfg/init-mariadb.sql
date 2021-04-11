grant all privileges on *.* TO 'root'@'%' IDENTIFIED BY 'root' WITH GRANT OPTION;

INSERT INTO tenants (appcode, app_settings, created_by, date_created, date_updated, display_name, encryption_key, primary_owner_id, primary_owner_name, secondary_owner_id, secondary_owner_name, status, updated_by)
    VALUES ('alphamail',null,'christian',now(),null,'Alphamail','','Christian','Christian','Christian','Christian','ACTIVE',null);
