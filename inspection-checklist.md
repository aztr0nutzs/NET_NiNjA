# Project Inspection Checklist (Build-owner grade)

Use this when you (or an AI) touches the codebase and claims it’s “done”.

## A) Build & CI
- [ ] `./gradlew build` passes on Linux + Windows
- [ ] `./gradlew test` passes
- [ ] `npm ci && npm run build` passes in `app-ui/web`
- [ ] No deprecated Gradle warnings promoted to errors in CI
- [ ] Dependency versions pinned in `gradle/libs.versions.toml`

## B) Runtime sanity
- [ ] Engine starts in <3s on a normal laptop
- [ ] `GET /api/v1/health` returns OK
- [ ] UI loads and renders with empty state (no hard crash)
- [ ] UI handles engine offline with a friendly banner

## C) Discovery correctness
- [ ] Scan requests validate CIDR
- [ ] Scan jobs are cancellable
- [ ] Results stream progressively
- [ ] Rate limiting prevents network abuse
- [ ] Provider failures are isolated (one provider can fail without killing scan)

## D) Storage correctness
- [ ] Schema migrations are versioned
- [ ] Unique constraints prevent duplicate devices
- [ ] Device identity rules documented and tested
- [ ] DB file location is predictable and configurable

## E) Security basics
- [ ] No secrets in logs
- [ ] Actions require explicit user intent
- [ ] CORS restricted for production
- [ ] Audit log present for actions and auth events

## F) UX quality
- [ ] Every feature has an obvious entry point
- [ ] “No devices yet” empty state explains next steps
- [ ] Errors are actionable (not stack traces)
- [ ] Bulk actions always show a confirmation + summary
