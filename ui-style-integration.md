# UI Style Integration (desired_visuals.html)

You asked for the dashboard to match the **exact visuals** from `desired_visuals.html`.
Computers cannot “guess” your CSS from vibes, so here’s the deterministic flow.

## Where the file goes
Place your file at:
- `app-ui/web/public/desired_visuals.html`

## Extract CSS into the dashboard
From `app-ui/web/`:
```bash
npm run theme:extract
```

What it does:
- reads `public/desired_visuals.html`
- extracts:
  - inline `<style>` blocks
  - linked stylesheets if they are local relative paths
- writes:
  - `src/styles/desired-visuals.css`
- ensures the app imports that stylesheet first.

## If your HTML references external CSS
Put the referenced CSS files under:
- `app-ui/web/public/visuals/`

…and update the `<link href="...">` paths in `desired_visuals.html` to be relative to `/visuals/...`.

## Important constraint
This project **does not copy layout HTML** from your file.
It uses the CSS theme (variables, classes, typography) and applies it to React components.
That keeps the app maintainable instead of fossilizing it into a single hand-edited HTML page.
