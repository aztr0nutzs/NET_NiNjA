# Phase 0–9 Build Guide + Checklist

This is a guided build plan that assumes you want **real progress**, not a folder full of wishful thinking.

## Phase 0: Repo bootstrap (this zip)
**Goal:** Everyone can build & run the baseline.

Checklist:
- [ ] `./gradlew :core-engine:server:run` starts
- [ ] `app-ui/web` runs `npm run dev`
- [ ] UI loads and can call `GET /api/v1/health`
- [ ] CI passes on a clean machine

## Phase 1: Network inventory foundation
**Goal:** Persist devices and show them in UI.

Checklist:
- [ ] Define canonical device identity rules (MAC preferred, fallback to stable keys)
- [ ] Store IP history, first_seen, last_seen, online state
- [ ] UI device table: search/sort/filter
- [ ] Device detail drawer: “facts”, “history”, “actions”

## Phase 2: Discovery v1 (fast + reliable)
**Goal:** Multi-signal discovery that works on common home/office networks.

Checklist:
- [ ] Subnet inference (desktop)
- [ ] Concurrent reachability sweep with rate limiting
- [ ] ARP parsing (Linux + Windows)
- [ ] mDNS scan (already scaffolded)
- [ ] Results stream to UI (WebSocket) with progress

## Phase 3: Admin actions v1
**Goal:** Safe, useful control features.

Checklist:
- [ ] Ping action (already scaffolded)
- [ ] Wake-on-LAN action
- [ ] HTTP health check action
- [ ] Actions UI: bulk run + per-device run + logs
- [ ] Guardrails: confirmation for bulk ops

## Phase 4: Automation & schedules
**Goal:** Make it feel “automatic”.

Checklist:
- [ ] Scheduled scans (cron-like config)
- [ ] Rules engine v1: (if device matches filter) then (tag / alert / action)
- [ ] Notification channels: local UI + webhook

## Phase 5: Identity, auth, and roles
**Goal:** Stop pretending “localhost = secure”.

Checklist:
- [ ] Local token auth (dev)
- [ ] Roles: viewer/admin
- [ ] Audit log: who ran what, when, outcome

## Phase 6: Secrets + credential storage
**Goal:** Store credentials safely (or don’t store them).

Checklist:
- [ ] Per-user encrypted secret store
- [ ] Never log secrets
- [ ] Credential scopes per action/provider

## Phase 7: Android integration
**Goal:** Useful Android experience.

Checklist:
- [ ] WebView wrapper stable
- [ ] Local discovery via `discovery-android`
- [ ] Optional: on-device engine (future) or remote engine pairing

## Phase 8: Packaging & installers
**Goal:** “Normal people can run it”.

Checklist:
- [ ] Windows: packaged engine + UI (Tauri/Electron or bundled server)
- [ ] Linux: AppImage/deb/rpm (pick one)
- [ ] Auto-update strategy (optional)

## Phase 9: Hardening & scale
**Goal:** Be boring in production (the highest compliment).

Checklist:
- [ ] Load tests for scan + UI streaming
- [ ] DB migrations + backups
- [ ] Plugin SDK for providers/actions
- [ ] Threat model review + pentest checklist
