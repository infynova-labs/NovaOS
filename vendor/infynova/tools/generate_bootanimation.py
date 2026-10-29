#!/usr/bin/env python3
"""
Create an uncompressed (store) ZIP suitable for AOSP bootanimation.zip.

Usage: python3 generate_bootanimation.py src_frames_dir desc.txt out_bootanimation.zip

This script will walk the `src_frames_dir`, add files preserving relative paths,
and write them into `out_bootanimation.zip` with ZIP_STORED (no compression),
which is required by some boot animation implementations.
"""
import sys
import os
import zipfile


def create_uncompressed_zip(src_dir, desc_path, out_zip):
    with zipfile.ZipFile(out_zip, 'w', compression=zipfile.ZIP_STORED) as zf:
        # add desc.txt first if provided
        if desc_path and os.path.isfile(desc_path):
            zf.write(desc_path, arcname=os.path.basename(desc_path))

        for root, dirs, files in os.walk(src_dir):
            for f in sorted(files):
                full = os.path.join(root, f)
                arc = os.path.relpath(full, src_dir)
                zf.write(full, arcname=arc)


def main():
    if len(sys.argv) < 4:
        print("Usage: generate_bootanimation.py <frames_dir> <desc.txt> <out.zip>")
        sys.exit(2)

    src = sys.argv[1]
    desc = sys.argv[2]
    out = sys.argv[3]

    if not os.path.isdir(src):
        print("Frames dir not found:", src)
        sys.exit(1)

    create_uncompressed_zip(src, desc, out)
    print("Created uncompressed bootanimation:", out)


if __name__ == '__main__':
    main()
