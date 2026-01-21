# Blueprint: Cross-Platform Network Discovery + Admin Control Suite

Humans love “one-click admin control” until they discover networks are messy, permissions are weird, and devices lie.
This blueprint keeps you honest and still ships something that works.

## 1) Product goals
### Must-haves
- **Fast, friendly dashboard**: search, filters, tags, bulk actions, zero “where do I click?” moments.
- **Discovery**: enumerate devices on local networks (LAN), label them, track changes.
- **Inventory**: OS guess, vendor, MAC/IP history, ports/services, mDNS/SSDP names when possible.
- **Control**: safe admin actions with guardrails (ping, wake-on-LAN, HTTP checks, SSH command runner, SNMP read).
- **Automation**: scheduled scans, alerts, simple playbooks (“when device appears, run X”).

### Non-goals (initially)
- Full NAC replacement, deep packet inspection, or “hack the planet” stuff.
- Silent privileged installs. If you need admin rights, you ask nicely and explain why.

## 2) Architecture overview

**UI (React/Vite)**  →  **Engine (Ktor)**  →  **Discovery providers** + **Storage**

### Modules (this repo)
- `app-ui/`  
  - `web/`: dashboard
  - `android-wrapper/`: Android WebView client that points to engine
- `core-model/`: OpenAPI schema, shared DTOs
- `core-engine/`: Ktor server exposing REST + WebSocket
- `discovery-desktop/`: LAN discovery providers for JVM (Linux/Windows)
- `discovery-android/`: Android native discovery helpers (optional)
- `storage/`: SQLite persistence layer (Exposed)

### Data flow
1. UI triggers scan: `POST /api/v1/discovery/scan`
2. Engine dispatches to providers and streams results to:
   - storage (device inventory)
   - UI via WebSocket job channel
3. UI renders devices with sorting/filtering/bulk actions.

## 3) Discovery strategy (layered, best-effort)
Use multiple signals. Don’t rely on a single “magic scan”.

1. **Local subnet estimation**
   - Desktop: infer from network interfaces (future)
   - Android: infer from WifiManager DHCP info
2. **Reachability sweep**
   - ICMP where possible, TCP connect fallback
3. **ARP table parsing**
   - Fast MAC + IP mapping (OS-specific)
4. **mDNS (Bonjour)**
   - Hostnames, service types
5. **SSDP/UPnP**
   - Media devices, routers, IoT hubs
6. **Optional SNMP**
   - If credentials provided, get model/uptime/interface info

## 4) Admin control strategy (safe by default)
Actions are opt-in, permissioned, and logged.
- **Ping**
- **Wake-on-LAN**
- **HTTP/S health check**
- **SSH command** (requires user-supplied credentials; never store plaintext)
- **SNMP GET** (read-only by default)
- **Pluggable actions** interface (so you can add WMI/WinRM later)

## 5) Security model (don’t be reckless)
- Use **role-based access**: admin vs viewer.
- API tokens for local UI use; for remote access prefer mTLS or OAuth later.
- Never auto-run destructive actions.
- Store secrets encrypted-at-rest (phase 6+).

## 6) UX principles
- A single **Scan** button that also has an “Advanced” dropdown.
- Every page has:
  - Search box
  - Filter chips (OS, vendor, tags, online/offline)
  - Bulk actions bar
- “Explain like I’m tired” tooltips.
- “Undo” where practical: tags, metadata, rule edits.

## 7) Performance goals
- Scan results stream within 1–2 seconds for the first devices.
- Don’t freeze UI: everything async + incremental rendering.
- Cache vendor/OUI lookups locally.

## 8) Extensibility
- Providers: `NetworkDiscoveryProvider` interface
- Actions: `DeviceAction` interface
- Storage: repository interfaces
- API: OpenAPI-first so TS and Kotlin clients stay aligned.

## 9) What this starter repo already gives you
- A working Ktor server
- Basic ping sweep provider
- Basic mDNS discovery provider (JmDNS)
- SQLite storage + simple schema
- React dashboard skeleton + theme extraction script
- Android wrapper app + Android discovery library skeleton
