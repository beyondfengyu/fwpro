#!/bin/bash
# @Function : Sending generated files to upstream web servers.
# @Prompt : 
#	1.The servers which accept sending files have to be deployed rsync deamon!
#	2.This file should not be modified frequently.You must be sure the "line break" is "\n" instead of "\r\n"!!!

page_file_path="/data/webapps/admin_cont_cgpage/"
server_ips=(127.0.0.1 192.168.75.130)  			#Servers' ip which are going to accept files.

exit_code="0"
exit_code_tmp="0"

for ip in ${server_ips[@]};
do
    rsync -azP --delete $page_file_path cont@$ip::cont_page
    exit_code_tmp="$?"
    if [ $exit_code -eq "0" ]; then
        exit_code="$exit_code_tmp"
    fi
done

echo "exit:$exit_code"