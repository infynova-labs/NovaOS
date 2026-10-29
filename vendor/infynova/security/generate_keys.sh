#!/bin/bash
# Script to generate NovaOS production release keys
# Usage: ./generate_keys.sh "/C=US/ST=California/L=Mountain View/O=InfYNova/OU=NovaOS/CN=NovaOS/emailAddress=security@novaos.io"

AUTH_SUBJECT=$1

if [ -z "$AUTH_SUBJECT" ]; then
    echo "Usage: $0 \"<subject>\""
    echo "Example: $0 \"/C=US/ST=California/L=Mountain View/O=InfYNova/OU=NovaOS/CN=NovaOS/emailAddress=security@novaos.io\""
    exit 1
fi

MAKE_KEY="../../../development/tools/make_key"

for key in releasekey platform shared media networkstack; do
    echo "Generating $key..."
    $MAKE_KEY $key "$AUTH_SUBJECT"
done

echo "Keys generated in $(pwd)"
