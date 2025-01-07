class Auth::TokenService
  class << self
    def issue_tokens(user, device_info = nil)
      access_token = generate_access_token(user)
      refresh_token = generate_refresh_token(user, device_info)

      [ access_token, refresh_token ]
    end

    def refresh_tokens(token, device_info = nil)
      begin
        payload = Auth::JwtService.decode(token)[0]
        return nil unless payload

        user = User.find_by(id: payload['sub'].to_i)
        return nil unless user

        refresh_token = user.refresh_tokens.find_by(token: token)
        if refresh_token.active?
          refresh_token.revoke!
          access_token, refresh_token = issue_tokens(user, device_info)

          [ user, access_token, refresh_token ]
        else
          # TODO: 유효하지 않은(ex.만료된) refresh_token으로 갱신 요청했을 때 처리 필요
          nil
        end
      rescue JWT::ExpiredSignature
        render json: { error: 'Refresh token has expired' }, status: :unauthorized
      rescue JWT::DecodeError
        render json: { error: 'Invalid refresh token' }, status: :unauthorized
      end
    end

    private

    def generate_access_token(user)
      Warden::JWTAuth::UserEncoder.new.call(user, :user, nil).first
    end

    def generate_refresh_token(user, device_info = nil)
      payload = {
        sub: user.id,
        scp: 'user',
        aud: 'refresh',
        iat: Time.now.to_i,
        exp: 2.weeks.from_now.to_i
      }

      refresh_token = Auth::JwtService.encode(payload)

      user.refresh_tokens.create!(
        token: refresh_token,
        device_info: device_info,
        expires_at: 2.weeks.from_now)

      refresh_token
    end
  end
end
