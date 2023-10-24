






Rename the following files/folders:

settings.gradle 
    - change name to plugin name
build.gradle 
    - change mainClassName to the package name + the main class (com.example.template.Main)
    - change serverPluginsDirPath to the path to your server's plugins folder
src/main/resources/plugin.yml
    - set name to plugin name, no caps
    - set main to path to the package name + the main class (com.example.template.Main)



Package Name:
Your package name should be in the format "<reversed_domain>.<plugin_name>".
For example, if my domain was "example.com", and my plugin name was "template", 
my package name would be "com.example.template". If you have no domain, use
"<yourname>.me", for example; "me.jeffory.template". It should have no spaces.
In this format, it will comply with java package naming standards.