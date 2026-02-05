FROM oven/bun:1.3.8-debian AS base

# Used for health checks
RUN apt update
RUN apt install -y curl

# Set the environment
ENV NEXT_PUBLIC_APP_ENV=production

# -----------------------------------

# Install dependencies
FROM base AS depends
WORKDIR /app
COPY . .
RUN bun install --frozen-lockfile

# Build the web app
RUN bun i -g typescript
RUN bun run build

# Run the app
FROM base AS runner
WORKDIR /app

# -----------------------------------

COPY --from=depends /app/node_modules ./node_modules

# Copy the built files
COPY --from=depends /app/public ./public
COPY --from=depends /app/.next/standalone/ ./
COPY --from=depends /app/.next/static ./.next/static

# Expose the app port
EXPOSE 3000
ENV HOSTNAME="0.0.0.0"
ENV PORT=3000

# Use the correct path to server.js
CMD ["bun", "--bun", "--smol", "server.js"]