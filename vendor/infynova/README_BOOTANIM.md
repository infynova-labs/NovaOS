Bootanimation packaging

Place your boot animation frames in a directory (e.g. `vendor/infynova/assets/bootanimation/1080p/`) and a `desc.txt` alongside.
Then run:

```bash
python3 vendor/infynova/tools/generate_bootanimation.py \
  vendor/infynova/assets/bootanimation/1080p/ vendor/infynova/assets/bootanimation/desc.txt \
  vendor/infynova/assets/bootanimation.zip
```

This produces an uncompressed `bootanimation.zip` suitable for copying into the product image.
