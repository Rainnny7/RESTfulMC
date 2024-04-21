FROM imbios/bun-node AS base


# Install dependencies
FROM base AS depends
WORKDIR /usr/src/app
COPY package.json* bun.lockb* ./
RUN bun install --frozen-lockfile --quiet


# Build the app
FROM base AS builder
WORKDIR /usr/src/app
COPY --from=depends /usr/src/app/node_modules ./node_modules
COPY . .
ENV NEXT_TELEMETRY_DISABLED 1
RUN bun run build


# Run the app
FROM base AS runner
WORKDIR /usr/src/app

RUN addgroup --system --gid 1007 nextjs
RUN adduser --system --uid 1007 nextjs

RUN mkdir .next
RUN chown nextjs:nextjs .next

COPY --from=builder --chown=nextjs:nextjs /usr/src/app/node_modules ./node_modules
COPY --from=builder --chown=nextjs:nextjs /usr/src/app/.next ./.next
COPY --from=builder --chown=nextjs:nextjs /usr/src/app/public ./public
COPY --from=builder --chown=nextjs:nextjs /usr/src/app/docs ./docs
COPY --from=builder --chown=nextjs:nextjs /usr/src/app/next.config.mjs ./next.config.mjs
COPY --from=builder --chown=nextjs:nextjs /usr/src/app/package.json ./package.json

ENV NODE_ENV production

# Exposting on port 80 so we can
# access via a reverse proxy for Dokku
ENV HOSTNAME "0.0.0.0"
EXPOSE 80
ENV PORT 80

USER nextjs
CMD ["bun", "start"]