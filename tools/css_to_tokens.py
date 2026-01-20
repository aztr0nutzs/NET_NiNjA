#!/usr/bin/env python3
"""Extract CSS custom properties (--vars) into JSON.

Usage:
  python tools/css_to_tokens.py design/style-reference.css design/tokens.json
"""

import json, re, sys
from pathlib import Path

VAR_RE = re.compile(r"--([a-zA-Z0-9_-]+)\s*:\s*([^;]+);")

def main():
    if len(sys.argv) != 3:
        print("Usage: css_to_tokens.py <input.css> <output.json>")
        sys.exit(2)

    css_path = Path(sys.argv[1])
    out_path = Path(sys.argv[2])

    css = css_path.read_text(encoding="utf-8", errors="ignore")
    tokens = {}
    for m in VAR_RE.finditer(css):
        tokens[m.group(1).strip()] = m.group(2).strip()

    out_path.parent.mkdir(parents=True, exist_ok=True)
    out_path.write_text(json.dumps(tokens, indent=2, sort_keys=True), encoding="utf-8")
    print(f"Wrote {len(tokens)} tokens to {out_path}")

if __name__ == "__main__":
    main()
